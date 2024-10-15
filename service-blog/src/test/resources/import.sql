-- DELETE FROM content_blog WHERE title IN ('The Evolution of Tech', 'Sustainability in Business');


INSERT INTO content_blog (user_id, title, description, content, status, created_at, updated_at) 
VALUES (1, 'The Evolution of Tech', 'A detailed exploration of technological advances over the last decade.', 
        'This blog post delves into the major innovations that have transformed industries.', 
        'PUBLISHED', '2024-10-11 10:45:00', '2024-10-11 10:45:00');

INSERT INTO content_blog (user_id, title, description, content, status, created_at, updated_at) 
VALUES (2, 'Sustainability in Business', 'How companies can integrate sustainability into their business models.', 
        'This post discusses strategies for building a more sustainable future through business practices.', 
        'PUBLISHED', '2024-10-11 10:45:00', '2024-10-11 10:45:00');
        
INSERT INTO content_blog (user_id, title, description, content, status, created_at, updated_at) 
VALUES (2, 'duplicado', 'duplicado', 
        'duplicado', 
        'PUBLISHED', '2024-10-11 10:45:00', '2024-10-11 10:45:00');
