CREATE TABLE actors_by_id_json (
                      id uuid PRIMARY KEY ,
                      actor text )
WITH compaction = {'class': 'SizeTieredCompactionStrategy'};

CREATE TABLE actors_by_id_object(
                      id uuid PRIMARY KEY  ,
                      actor frozen < actor> )
WITH compaction = {'class': 'SizeTieredCompactionStrategy'};