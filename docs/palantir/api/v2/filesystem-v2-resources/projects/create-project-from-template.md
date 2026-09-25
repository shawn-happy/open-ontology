`POST /api/v2/filesystem/projects/createFromTemplate`

Creates a project from a project template.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Request body

```json
{
  "variableValues": {
    "name": "my project name"
  },
  "organizationRids": [
    "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
  ],
  "defaultRoles": [
    "8bf49052-dc37-4528-8bf0-b551cfb71268"
  ],
  "templateRid": "ri.compass.main.template.c410f510-2937-420e-8ea3-8c9bcb3c1791"
}
```

## Response

**Project**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Project` | object | 是 | 示例: `{"path":"/Empyrean Airlines/My Important Project","updatedTime":"2024-09-25T17:29:35.974Z","updatedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","displayName":"My Important Project","documentation":"project documentation","resourceLevelRoleGrantsAllowed":true,"description":"project description","createdTime":"2024-09-25T17:29:35.974Z","rid":"ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8","spaceRid":"ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca","trashStatus":"NOT_TRASHED"}` |
| `Project.rid` | string | 是 | The unique resource identifier (RID) of a Project.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |
| `Project.displayName` | string | 是 | The display name of the Project. Must be unique and cannot contain a /<br>示例: `My Important Project` |
| `Project.description` | string | 否 | The description associated with the Project.<br>示例: `project description` |
| `Project.documentation` | string | 否 | The documentation associated with the Project.<br>示例: `project documentation` |
| `Project.path` | string | 是 | The full path to the resource, including the resource name itself<br>示例: `/Empyrean Airlines/My Important Project` |
| `Project.createdBy` | string | 是 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Project.updatedBy` | string | 是 | The Foundry user who last updated this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Project.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Project.updatedTime` | string | 是 | The time at which the resource was most recently updated.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Project.trashStatus` | enum | 是 | The trash status of the Project.<br>示例: `NOT_TRASHED` |
| `Project.spaceRid` | string | 是 | The Space Resource Identifier (RID) that the Project lives in.<br>示例: `ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca` |
| `Project.resourceLevelRoleGrantsAllowed` | boolean | 是 | Whether role grants are allowed on individual resources within the Project.<br>示例: `true` |

```json
{
  "path": "/Empyrean Airlines/My Important Project",
  "updatedTime": "2024-09-25T17:29:35.974Z",
  "updatedBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "displayName": "My Important Project",
  "documentation": "project documentation",
  "resourceLevelRoleGrantsAllowed": true,
  "description": "project description",
  "createdTime": "2024-09-25T17:29:35.974Z",
  "rid": "ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8",
  "spaceRid": "ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca",
  "trashStatus": "NOT_TRASHED"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ProjectTemplateNotFound` | The project template RID referenced cannot be found. |
| INVALID_ARGUMENT | `DefaultRolesNotInSpaceRoleSet` | The requested default roles are not in the role set of the space for the project template. |
| INVALID_ARGUMENT | `NotAuthorizedToApplyOrganization` | The user is not authorized to apply at least one of the organization markings required to create the project from template. |
| INVALID_ARGUMENT | `InvalidOrganizationHierarchy` | Organizations on a project must also exist on the parent space. This error is thrown if the configuration<br>of a project's organizations (on creation or subsequently) results in the project being marked with either<br>no organizations in a marked space, or with an organization that is not present on the parent space. |
| INVALID_ARGUMENT | `CreateProjectNoOwnerLikeRoleGrant` | The create project request would create a project with no principal being granted an owner-like role. As a result, there would be no user with administrative privileges over the project. A role is defined to be owner-like if it has the `compass:edit-project` operation. In the common case of the default role-set, this is just the `compass:manage` role. |
| PERMISSION_DENIED | `CreateGroupPermissionDenied` | The user is not authorized to create the group in the organization required to create the project from template. |
| PERMISSION_DENIED | `AddGroupToParentGroupPermissionDenied` | The user is not authorized to add a a group to the parent group required to create the project from template. |
| CONFLICT | `TemplateGroupNameConflict` | Creating the project from template would attempt to create new groups with names conflicting either with other new groups, or existing groups. |
| CONFLICT | `TemplateMarkingNameConflict` | Creating the project from template would attempt to create new markings with names conflicting either with other new markings, or existing markings. |
| INVALID_ARGUMENT | `InvalidPrincipalIdsForGroupTemplate` | The template requested for project creation contains principal IDs that do not exist. |
| INVALID_ARGUMENT | `InvalidDescription` | Either the user has not passed a value for a template with unset project description, or has passed a value for a template with fixed project description. |
| INVALID_ARGUMENT | `InvalidOrganizations` | Either the user has not passed organizations for a template with suggested organizations, or has passed organization for a template with fixed organizations. |
| INVALID_ARGUMENT | `MissingVariableValue` | A variable defined on the template requested for project creation does not have a value set in the request. |
| INVALID_ARGUMENT | `InvalidVariable` | A variable referenced in the request to create project from template is not defined on the template. |
| INVALID_ARGUMENT | `InvalidVariableEnumOption` | The value passed in the request to create project from template for an enum type variable is not a valid option. |
| NOT_FOUND | `OrganizationsNotFound` | At least one organization RID could not be found. |
| INVALID_ARGUMENT | `InvalidDefaultRoles` | Either the user has not passed default roles for a template with suggested default roles, or has passed default roles for a template with fixed default roles. |
| PERMISSION_DENIED | `CreateProjectFromTemplatePermissionDenied` | Could not createFromTemplate the Project. |
| NOT_FOUND | `ProjectNotFound` | The given Project could not be found. |
