CREATE ROLE air_schema_owner WITH LOGIN PASSWORD 'CHANGEME';
GRANT air_schema_owner TO db_ref_air_owner;

CREATE SCHEMA air_api       AUTHORIZATION air_schema_owner;

CREATE ROLE air_ta WITH LOGIN PASSWORD 'CHANGEME';
GRANT air_ta TO air_schema_owner;

GRANT CONNECT ON DATABASE db_ref_air TO air_ta;

GRANT CREATE ON SCHEMA air_api       TO air_ta;

GRANT USAGE ON SCHEMA air_api       TO air_ta;

GRANT ALL ON ALL TABLES IN SCHEMA air_api       TO air_ta;

ALTER DEFAULT PRIVILEGES FOR USER air_schema_owner IN SCHEMA air_api       GRANT USAGE ON SEQUENCES TO air_ta;

ALTER DEFAULT PRIVILEGES FOR USER air_schema_owner IN SCHEMA air_api       GRANT EXECUTE ON FUNCTIONS TO air_ta;