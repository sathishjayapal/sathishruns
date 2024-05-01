-- Add a new column with bigint type
ALTER TABLE activities ADD COLUMN activityID_new bigint;

-- Copy and convert the data from the old column to the new one
UPDATE activities SET activityID_new = CAST(activityID AS bigint);

-- Drop the old column
ALTER TABLE activities DROP COLUMN activityID;

-- Rename the new column to the old column's name
ALTER TABLE activities RENAME COLUMN activityID_new TO activityID;
