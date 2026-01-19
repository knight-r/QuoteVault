-- QuoteVault Seed Data
-- Run this after schema.sql

-- Insert Categories
INSERT INTO categories (id, name, display_name, icon_name, color_hex, sort_order) VALUES
    ('c1000000-0000-0000-0000-000000000001', 'motivation', 'Motivation', 'bolt', '#FF6B35', 1),
    ('c1000000-0000-0000-0000-000000000002', 'love', 'Love', 'favorite', '#EC4899', 2),
    ('c1000000-0000-0000-0000-000000000003', 'success', 'Success', 'emoji_events', '#10B981', 3),
    ('c1000000-0000-0000-0000-000000000004', 'wisdom', 'Wisdom', 'psychology', '#8B5CF6', 4),
    ('c1000000-0000-0000-0000-000000000005', 'humor', 'Humor', 'sentiment_very_satisfied', '#F59E0B', 5);

-- Insert Motivation Quotes (25)
INSERT INTO quotes (text, author, category_id, is_featured) VALUES
    ('The only way to do great work is to love what you do.', 'Steve Jobs', 'c1000000-0000-0000-0000-000000000001', true),
    ('Believe you can and you''re halfway there.', 'Theodore Roosevelt', 'c1000000-0000-0000-0000-000000000001', true),
    ('Success is not final, failure is not fatal: it is the courage to continue that counts.', 'Winston Churchill', 'c1000000-0000-0000-0000-000000000001', true),
    ('The future belongs to those who believe in the beauty of their dreams.', 'Eleanor Roosevelt', 'c1000000-0000-0000-0000-000000000001', false),
    ('It does not matter how slowly you go as long as you do not stop.', 'Confucius', 'c1000000-0000-0000-0000-000000000001', false),
    ('Everything you''ve ever wanted is on the other side of fear.', 'George Addair', 'c1000000-0000-0000-0000-000000000001', false),
    ('The only impossible journey is the one you never begin.', 'Tony Robbins', 'c1000000-0000-0000-0000-000000000001', false),
    ('Dream big and dare to fail.', 'Norman Vaughan', 'c1000000-0000-0000-0000-000000000001', false),
    ('What lies behind us and what lies before us are tiny matters compared to what lies within us.', 'Ralph Waldo Emerson', 'c1000000-0000-0000-0000-000000000001', false),
    ('The best time to plant a tree was 20 years ago. The second best time is now.', 'Chinese Proverb', 'c1000000-0000-0000-0000-000000000001', true),
    ('Your limitation—it''s only your imagination.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Push yourself, because no one else is going to do it for you.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Great things never come from comfort zones.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Don''t stop when you''re tired. Stop when you''re done.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Wake up with determination. Go to bed with satisfaction.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Do something today that your future self will thank you for.', 'Sean Patrick Flanery', 'c1000000-0000-0000-0000-000000000001', false),
    ('Little things make big days.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('It''s going to be hard, but hard does not mean impossible.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Don''t wait for opportunity. Create it.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Sometimes we''re tested not to show our weaknesses, but to discover our strengths.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('The key to success is to focus on goals, not obstacles.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Dream it. Wish it. Do it.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Success doesn''t just find you. You have to go out and get it.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('The harder you work for something, the greater you''ll feel when you achieve it.', 'Unknown', 'c1000000-0000-0000-0000-000000000001', false),
    ('Don''t be afraid to give up the good to go for the great.', 'John D. Rockefeller', 'c1000000-0000-0000-0000-000000000001', false);

-- Insert Love Quotes (20)
INSERT INTO quotes (text, author, category_id, is_featured) VALUES
    ('The best thing to hold onto in life is each other.', 'Audrey Hepburn', 'c1000000-0000-0000-0000-000000000002', true),
    ('Love is composed of a single soul inhabiting two bodies.', 'Aristotle', 'c1000000-0000-0000-0000-000000000002', true),
    ('Where there is love there is life.', 'Mahatma Gandhi', 'c1000000-0000-0000-0000-000000000002', true),
    ('Being deeply loved by someone gives you strength, while loving someone deeply gives you courage.', 'Lao Tzu', 'c1000000-0000-0000-0000-000000000002', false),
    ('The greatest thing you''ll ever learn is just to love and be loved in return.', 'Eden Ahbez', 'c1000000-0000-0000-0000-000000000002', false),
    ('Love is not about how many days, months, or years you have been together. Love is about how much you love each other every single day.', 'Unknown', 'c1000000-0000-0000-0000-000000000002', false),
    ('In all the world, there is no heart for me like yours. In all the world, there is no love for you like mine.', 'Maya Angelou', 'c1000000-0000-0000-0000-000000000002', false),
    ('You know you''re in love when you can''t fall asleep because reality is finally better than your dreams.', 'Dr. Seuss', 'c1000000-0000-0000-0000-000000000002', true),
    ('Love recognizes no barriers. It jumps hurdles, leaps fences, penetrates walls to arrive at its destination full of hope.', 'Maya Angelou', 'c1000000-0000-0000-0000-000000000002', false),
    ('To love and be loved is to feel the sun from both sides.', 'David Viscott', 'c1000000-0000-0000-0000-000000000002', false),
    ('Love is when the other person''s happiness is more important than your own.', 'H. Jackson Brown Jr.', 'c1000000-0000-0000-0000-000000000002', false),
    ('The heart wants what it wants. There''s no logic to these things.', 'Woody Allen', 'c1000000-0000-0000-0000-000000000002', false),
    ('I have found the paradox, that if you love until it hurts, there can be no more hurt, only more love.', 'Mother Teresa', 'c1000000-0000-0000-0000-000000000002', false),
    ('Love is friendship that has caught fire.', 'Ann Landers', 'c1000000-0000-0000-0000-000000000002', false),
    ('The best love is the kind that awakens the soul and makes us reach for more.', 'Nicholas Sparks', 'c1000000-0000-0000-0000-000000000002', false),
    ('Love is a canvas furnished by nature and embroidered by imagination.', 'Voltaire', 'c1000000-0000-0000-0000-000000000002', false),
    ('We are most alive when we''re in love.', 'John Updike', 'c1000000-0000-0000-0000-000000000002', false),
    ('Love is an untamed force. When we try to control it, it destroys us.', 'Paulo Coelho', 'c1000000-0000-0000-0000-000000000002', false),
    ('Keep love in your heart. A life without it is like a sunless garden when the flowers are dead.', 'Oscar Wilde', 'c1000000-0000-0000-0000-000000000002', false),
    ('One word frees us of all the weight and pain of life: that word is love.', 'Sophocles', 'c1000000-0000-0000-0000-000000000002', false);

-- Insert Success Quotes (20)
INSERT INTO quotes (text, author, category_id, is_featured) VALUES
    ('Success is not the key to happiness. Happiness is the key to success.', 'Albert Schweitzer', 'c1000000-0000-0000-0000-000000000003', true),
    ('The road to success and the road to failure are almost exactly the same.', 'Colin R. Davis', 'c1000000-0000-0000-0000-000000000003', false),
    ('Success usually comes to those who are too busy to be looking for it.', 'Henry David Thoreau', 'c1000000-0000-0000-0000-000000000003', true),
    ('Don''t be afraid to give up the good to go for the great.', 'John D. Rockefeller', 'c1000000-0000-0000-0000-000000000003', false),
    ('I find that the harder I work, the more luck I seem to have.', 'Thomas Jefferson', 'c1000000-0000-0000-0000-000000000003', false),
    ('Success is walking from failure to failure with no loss of enthusiasm.', 'Winston Churchill', 'c1000000-0000-0000-0000-000000000003', true),
    ('The secret of success is to do the common thing uncommonly well.', 'John D. Rockefeller Jr.', 'c1000000-0000-0000-0000-000000000003', false),
    ('Success seems to be connected with action. Successful people keep moving.', 'Conrad Hilton', 'c1000000-0000-0000-0000-000000000003', false),
    ('The only place where success comes before work is in the dictionary.', 'Vidal Sassoon', 'c1000000-0000-0000-0000-000000000003', false),
    ('If you really look closely, most overnight successes took a long time.', 'Steve Jobs', 'c1000000-0000-0000-0000-000000000003', false),
    ('There are no secrets to success. It is the result of preparation, hard work, and learning from failure.', 'Colin Powell', 'c1000000-0000-0000-0000-000000000003', false),
    ('Success is how high you bounce when you hit bottom.', 'George S. Patton', 'c1000000-0000-0000-0000-000000000003', false),
    ('The difference between a successful person and others is not a lack of strength, not a lack of knowledge, but rather a lack in will.', 'Vince Lombardi', 'c1000000-0000-0000-0000-000000000003', false),
    ('Success is not measured by what you accomplish, but by the opposition you have encountered.', 'Orison Swett Marden', 'c1000000-0000-0000-0000-000000000003', false),
    ('I never dreamed about success. I worked for it.', 'Estée Lauder', 'c1000000-0000-0000-0000-000000000003', false),
    ('Success is the sum of small efforts, repeated day in and day out.', 'Robert Collier', 'c1000000-0000-0000-0000-000000000003', false),
    ('All progress takes place outside the comfort zone.', 'Michael John Bobak', 'c1000000-0000-0000-0000-000000000003', false),
    ('Would you like me to give you a formula for success? Double your rate of failure.', 'Thomas J. Watson', 'c1000000-0000-0000-0000-000000000003', false),
    ('The real test is not whether you avoid this failure, but whether you let it harden or shame you into inaction.', 'Barack Obama', 'c1000000-0000-0000-0000-000000000003', false),
    ('Success is liking yourself, liking what you do, and liking how you do it.', 'Maya Angelou', 'c1000000-0000-0000-0000-000000000003', false);

-- Insert Wisdom Quotes (20)
INSERT INTO quotes (text, author, category_id, is_featured) VALUES
    ('The only true wisdom is in knowing you know nothing.', 'Socrates', 'c1000000-0000-0000-0000-000000000004', true),
    ('In the middle of difficulty lies opportunity.', 'Albert Einstein', 'c1000000-0000-0000-0000-000000000004', true),
    ('The mind is everything. What you think you become.', 'Buddha', 'c1000000-0000-0000-0000-000000000004', true),
    ('Knowing yourself is the beginning of all wisdom.', 'Aristotle', 'c1000000-0000-0000-0000-000000000004', false),
    ('The fool doth think he is wise, but the wise man knows himself to be a fool.', 'William Shakespeare', 'c1000000-0000-0000-0000-000000000004', false),
    ('The journey of a thousand miles begins with one step.', 'Lao Tzu', 'c1000000-0000-0000-0000-000000000004', true),
    ('It is not that I''m so smart. But I stay with the questions much longer.', 'Albert Einstein', 'c1000000-0000-0000-0000-000000000004', false),
    ('The unexamined life is not worth living.', 'Socrates', 'c1000000-0000-0000-0000-000000000004', false),
    ('Yesterday I was clever, so I wanted to change the world. Today I am wise, so I am changing myself.', 'Rumi', 'c1000000-0000-0000-0000-000000000004', false),
    ('Life is really simple, but we insist on making it complicated.', 'Confucius', 'c1000000-0000-0000-0000-000000000004', false),
    ('Be the change that you wish to see in the world.', 'Mahatma Gandhi', 'c1000000-0000-0000-0000-000000000004', false),
    ('The greatest glory in living lies not in never falling, but in rising every time we fall.', 'Nelson Mandela', 'c1000000-0000-0000-0000-000000000004', false),
    ('In three words I can sum up everything I''ve learned about life: it goes on.', 'Robert Frost', 'c1000000-0000-0000-0000-000000000004', false),
    ('The wise man does at once what the fool does finally.', 'Niccolo Machiavelli', 'c1000000-0000-0000-0000-000000000004', false),
    ('Turn your wounds into wisdom.', 'Oprah Winfrey', 'c1000000-0000-0000-0000-000000000004', false),
    ('We don''t see things as they are, we see them as we are.', 'Anaïs Nin', 'c1000000-0000-0000-0000-000000000004', false),
    ('The measure of intelligence is the ability to change.', 'Albert Einstein', 'c1000000-0000-0000-0000-000000000004', false),
    ('A wise man makes his own decisions, an ignorant man follows public opinion.', 'Grantland Rice', 'c1000000-0000-0000-0000-000000000004', false),
    ('Real knowledge is to know the extent of one''s ignorance.', 'Confucius', 'c1000000-0000-0000-0000-000000000004', false),
    ('Common sense is genius dressed in its working clothes.', 'Ralph Waldo Emerson', 'c1000000-0000-0000-0000-000000000004', false);

-- Insert Humor Quotes (20)
INSERT INTO quotes (text, author, category_id, is_featured) VALUES
    ('I''m not superstitious, but I am a little stitious.', 'Michael Scott', 'c1000000-0000-0000-0000-000000000005', true),
    ('Behind every great man is a woman rolling her eyes.', 'Jim Carrey', 'c1000000-0000-0000-0000-000000000005', false),
    ('I''m writing a book. I''ve got the page numbers done.', 'Steven Wright', 'c1000000-0000-0000-0000-000000000005', false),
    ('I used to think I was indecisive, but now I''m not so sure.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('I''m not arguing, I''m just explaining why I''m right.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('Life is short. Smile while you still have teeth.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', true),
    ('I''m not lazy, I''m on energy saving mode.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('My bed is a magical place where I suddenly remember everything I forgot to do.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('I''m on a seafood diet. I see food and I eat it.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('I don''t need a hair stylist, my pillow gives me a new hairstyle every morning.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('Be yourself; everyone else is already taken.', 'Oscar Wilde', 'c1000000-0000-0000-0000-000000000005', true),
    ('I''m not short, I''m just more down to earth than most people.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('The road to success is always under construction.', 'Lily Tomlin', 'c1000000-0000-0000-0000-000000000005', false),
    ('I''m not weird, I''m a limited edition.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('If at first you don''t succeed, then skydiving definitely isn''t for you.', 'Steven Wright', 'c1000000-0000-0000-0000-000000000005', false),
    ('Age is of no importance unless you''re a cheese.', 'Billie Burke', 'c1000000-0000-0000-0000-000000000005', false),
    ('I''m not clumsy, the floor just hates me.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('My wallet is like an onion. Opening it makes me cry.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false),
    ('I''m sorry, if you were right, I''d agree with you.', 'Robin Williams', 'c1000000-0000-0000-0000-000000000005', false),
    ('Common sense is like deodorant. The people who need it most never use it.', 'Unknown', 'c1000000-0000-0000-0000-000000000005', false);

-- Set initial quote of the day
SELECT set_daily_quote();
