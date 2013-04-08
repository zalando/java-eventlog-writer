RESET role;

CREATE SCHEMA zz_commons AUTHORIZATION zalando;

SET ROLE TO zalando;

GRANT USAGE ON SCHEMA zz_commons TO public;
GRANT USAGE ON SCHEMA zz_commons TO zalando_data_usage;

ALTER DEFAULT PRIVILEGES FOR ROLE zalando IN SCHEMA zz_commons GRANT EXECUTE ON FUNCTIONS TO public;
ALTER DEFAULT PRIVILEGES FOR ROLE zalando IN SCHEMA zz_commons GRANT SELECT ON SEQUENCES TO public;
ALTER DEFAULT PRIVILEGES FOR ROLE zalando IN SCHEMA zz_commons GRANT SELECT ON TABLES TO public;

ALTER DEFAULT PRIVILEGES FOR ROLE zalando IN SCHEMA zz_commons GRANT SELECT ON SEQUENCES TO zalando_data_reader;
ALTER DEFAULT PRIVILEGES FOR ROLE zalando IN SCHEMA zz_commons GRANT USAGE ON SEQUENCES TO zalando_data_writer;

ALTER DEFAULT PRIVILEGES FOR ROLE zalando IN SCHEMA zz_commons GRANT SELECT ON TABLES TO zalando_data_reader;
ALTER DEFAULT PRIVILEGES FOR ROLE zalando IN SCHEMA zz_commons GRANT INSERT, DELETE, UPDATE ON TABLES TO zalando_data_writer;
