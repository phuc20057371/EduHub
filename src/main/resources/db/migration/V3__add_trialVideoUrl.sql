-- Add trial_video_url column to training_program table
ALTER TABLE training_program 
ADD COLUMN trial_video_url VARCHAR(255);

-- Add trial_video_url column to training_unit table  
ALTER TABLE training_unit 
ADD COLUMN trial_video_url VARCHAR(255);