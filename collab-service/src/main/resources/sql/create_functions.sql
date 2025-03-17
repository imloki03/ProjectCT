USE projectct_collab;

-- PHASE related functions
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Create Phase', '^/project/phases/([^/]+)$', 'PHASE');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Update Phase', '^/project/phases/([^/]+)$', 'PHASE');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Delete Phase', '^/project/phases/([^/]+)$', 'PHASE');

-- TASK related functions
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Create Task', '^/project/tasks/([^/]+)$', 'TASK');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Edit Task', '^/project/tasks/([^/]+)$', 'TASK');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Update Task Status', '^/project/tasks/([^/]+)$', 'TASK');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Move Task to Phase', '^/project/tasks/move/([^/]+)/([^/]+)$', 'TASK');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Move Task to Backlog', '^/project/tasks/move/([^/]+)$', 'TASK');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Assign Task', '^/project/tasks/assign/([^/]+)/([^/]+)$', 'TASK');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Delete Task', '^/project/tasks/([^/]+)$', 'TASK');

-- STORAGE related functions
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Add Media', '^/storage/storages/media/([^/]+)$', 'STORAGE');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Update Media Info', '^/storage/storages/([^/]+)$', 'STORAGE');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Update Media Version', '^/storage/storages/([^/]+)$', 'STORAGE');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Delete Media', '^/storage/storages/([^/]+)$', 'STORAGE');

-- AUTH related functions (roles, functions, tokens, otp, login, password, etc.)
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Add Collaborator', '^/collab/collabs$', 'COLLABORATOR');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Update Collaborator Role', '^/collab/collabs/([^/]+)$', 'COLLABORATOR');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Remove Collaborator', '^/collab/collabs/([^/]+)$', 'COLLABORATOR');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Create Role', '^/collab/roles$', 'COLLABORATOR');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Update Role', '^/collab/roles/([^/]+)$', 'COLLABORATOR');
INSERT INTO app_function (name, endpoint, function_type) VALUES ('Delete Role', '^/collab/roles/([^/]+)$', 'COLLABORATOR');