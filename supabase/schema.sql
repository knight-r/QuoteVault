-- QuoteVault Database Schema
-- Run this in Supabase SQL Editor

-- Enable UUID extension
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Categories Table
CREATE TABLE categories (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(50) NOT NULL UNIQUE,
    display_name VARCHAR(100) NOT NULL,
    icon_name VARCHAR(50),
    color_hex VARCHAR(7) NOT NULL DEFAULT '#6750A4',
    sort_order INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Quotes Table
CREATE TABLE quotes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    text TEXT NOT NULL,
    author VARCHAR(255) NOT NULL,
    author_image_url TEXT,
    category_id UUID REFERENCES categories(id) ON DELETE SET NULL,
    source VARCHAR(255),
    is_featured BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Quote of the Day Table
CREATE TABLE quote_of_day (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    quote_id UUID NOT NULL REFERENCES quotes(id) ON DELETE CASCADE,
    display_date DATE NOT NULL UNIQUE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- User Profiles Table (extends Supabase auth.users)
CREATE TABLE user_profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    display_name VARCHAR(100),
    avatar_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- User Favorites Table
CREATE TABLE user_favorites (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    quote_id UUID NOT NULL REFERENCES quotes(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, quote_id)
);

-- Collections Table
CREATE TABLE collections (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES auth.users(id) ON DELETE CASCADE,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    cover_color VARCHAR(7) DEFAULT '#6750A4',
    is_public BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Collection Quotes Junction Table
CREATE TABLE collection_quotes (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    collection_id UUID NOT NULL REFERENCES collections(id) ON DELETE CASCADE,
    quote_id UUID NOT NULL REFERENCES quotes(id) ON DELETE CASCADE,
    added_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(collection_id, quote_id)
);

-- User Settings Table
CREATE TABLE user_settings (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL UNIQUE REFERENCES auth.users(id) ON DELETE CASCADE,
    theme_mode VARCHAR(20) DEFAULT 'SYSTEM',
    accent_color VARCHAR(20) DEFAULT 'DEFAULT',
    font_size VARCHAR(20) DEFAULT 'MEDIUM',
    notification_enabled BOOLEAN DEFAULT TRUE,
    notification_time TIME DEFAULT '09:00:00',
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Create indexes for better performance
CREATE INDEX idx_quotes_category ON quotes(category_id);
CREATE INDEX idx_quotes_featured ON quotes(is_featured);
CREATE INDEX idx_user_favorites_user ON user_favorites(user_id);
CREATE INDEX idx_user_favorites_quote ON user_favorites(quote_id);
CREATE INDEX idx_collections_user ON collections(user_id);
CREATE INDEX idx_collection_quotes_collection ON collection_quotes(collection_id);
CREATE INDEX idx_quote_of_day_date ON quote_of_day(display_date);

-- Row Level Security Policies

-- Enable RLS on all tables
ALTER TABLE categories ENABLE ROW LEVEL SECURITY;
ALTER TABLE quotes ENABLE ROW LEVEL SECURITY;
ALTER TABLE quote_of_day ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_profiles ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_favorites ENABLE ROW LEVEL SECURITY;
ALTER TABLE collections ENABLE ROW LEVEL SECURITY;
ALTER TABLE collection_quotes ENABLE ROW LEVEL SECURITY;
ALTER TABLE user_settings ENABLE ROW LEVEL SECURITY;

-- Categories: Public read
CREATE POLICY "Categories are viewable by everyone" ON categories
    FOR SELECT USING (true);

-- Quotes: Public read
CREATE POLICY "Quotes are viewable by everyone" ON quotes
    FOR SELECT USING (true);

-- Quote of Day: Public read
CREATE POLICY "Quote of day is viewable by everyone" ON quote_of_day
    FOR SELECT USING (true);

-- User Profiles: Users can view/update their own profile
CREATE POLICY "Users can view own profile" ON user_profiles
    FOR SELECT USING (auth.uid() = id);

CREATE POLICY "Users can update own profile" ON user_profiles
    FOR UPDATE USING (auth.uid() = id);

CREATE POLICY "Users can insert own profile" ON user_profiles
    FOR INSERT WITH CHECK (auth.uid() = id);

-- User Favorites: Users can manage their own favorites
CREATE POLICY "Users can view own favorites" ON user_favorites
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own favorites" ON user_favorites
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can delete own favorites" ON user_favorites
    FOR DELETE USING (auth.uid() = user_id);

-- Collections: Users can manage their own collections, view public ones
CREATE POLICY "Users can view own collections" ON collections
    FOR SELECT USING (auth.uid() = user_id OR is_public = true);

CREATE POLICY "Users can insert own collections" ON collections
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update own collections" ON collections
    FOR UPDATE USING (auth.uid() = user_id);

CREATE POLICY "Users can delete own collections" ON collections
    FOR DELETE USING (auth.uid() = user_id);

-- Collection Quotes: Users can manage quotes in their collections
CREATE POLICY "Users can view quotes in own collections" ON collection_quotes
    FOR SELECT USING (
        EXISTS (
            SELECT 1 FROM collections
            WHERE collections.id = collection_quotes.collection_id
            AND (collections.user_id = auth.uid() OR collections.is_public = true)
        )
    );

CREATE POLICY "Users can insert quotes in own collections" ON collection_quotes
    FOR INSERT WITH CHECK (
        EXISTS (
            SELECT 1 FROM collections
            WHERE collections.id = collection_quotes.collection_id
            AND collections.user_id = auth.uid()
        )
    );

CREATE POLICY "Users can delete quotes from own collections" ON collection_quotes
    FOR DELETE USING (
        EXISTS (
            SELECT 1 FROM collections
            WHERE collections.id = collection_quotes.collection_id
            AND collections.user_id = auth.uid()
        )
    );

-- User Settings: Users can manage their own settings
CREATE POLICY "Users can view own settings" ON user_settings
    FOR SELECT USING (auth.uid() = user_id);

CREATE POLICY "Users can insert own settings" ON user_settings
    FOR INSERT WITH CHECK (auth.uid() = user_id);

CREATE POLICY "Users can update own settings" ON user_settings
    FOR UPDATE USING (auth.uid() = user_id);

-- Function to create user profile on signup
CREATE OR REPLACE FUNCTION public.handle_new_user()
RETURNS TRIGGER AS $$
BEGIN
    INSERT INTO public.user_profiles (id, display_name)
    VALUES (NEW.id, NEW.raw_user_meta_data->>'display_name');

    INSERT INTO public.user_settings (user_id)
    VALUES (NEW.id);

    RETURN NEW;
END;
$$ LANGUAGE plpgsql SECURITY DEFINER;

-- Trigger to call function on new user
DROP TRIGGER IF EXISTS on_auth_user_created ON auth.users;
CREATE TRIGGER on_auth_user_created
    AFTER INSERT ON auth.users
    FOR EACH ROW EXECUTE FUNCTION public.handle_new_user();

-- Function to set quote of the day (call this daily via cron/edge function)
CREATE OR REPLACE FUNCTION set_daily_quote()
RETURNS void AS $$
DECLARE
    random_quote_id UUID;
BEGIN
    -- Get a random featured quote, or any random quote if no featured
    SELECT id INTO random_quote_id
    FROM quotes
    WHERE is_featured = true
    ORDER BY RANDOM()
    LIMIT 1;

    IF random_quote_id IS NULL THEN
        SELECT id INTO random_quote_id
        FROM quotes
        ORDER BY RANDOM()
        LIMIT 1;
    END IF;

    -- Insert or update quote of the day
    INSERT INTO quote_of_day (quote_id, display_date)
    VALUES (random_quote_id, CURRENT_DATE)
    ON CONFLICT (display_date) DO UPDATE
    SET quote_id = random_quote_id;
END;
$$ LANGUAGE plpgsql;
