`POST /api/v2/filesystem/projects/create`

Creates a new Project.

Note that third-party applications using this endpoint via OAuth2 cannot be associated with an
Ontology SDK as this will reduce the scope of operations to only those within specified projects.
When creating the application, select "No, I won't use an Ontology SDK" on the Resources page.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-write`.

**OAuth2 scopes**: `api:filesystem-write`

## Request body

```json
{
  "organizationRids": [
    "ri.multipass..organization.c30ee6ad-b5e4-4afe-a74f-fe4a289f2faa"
  ],
  "displayName": "My Important Project",
  "defaultRoles": [
    "8bf49052-dc37-4528-8bf0-b551cfb71268"
  ],
  "description": "project description",
  "roleGrants": {
    "8bf49052-dc37-4528-8bf0-b551cfb71268": [
      {
        "principalId": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
        "principalType": "GROUP"
      }
    ]
  },
  "spaceRid": "ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca"
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
| INVALID_ARGUMENT | `ProjectCreationNotSupported` | Project creation is not supported in the current user's space. |
| CONFLICT | `ProjectNameAlreadyExists` | The requested display name for the created project is already being used in the space. |
| INVALID_ARGUMENT | `InvalidDisplayName` | The display name of a resource should not be exactly `.` or `..`, contain a forward slash `/` and must be<br>less than or equal to 700 characters. |
| NOT_FOUND | `OrganizationsNotFound` | At least one organization RID could not be found. |
| INVALID_ARGUMENT | `InvalidRoleIds` | A roleId referenced in either default roles or role grants does not exist in the project role set for the space. |
| INVALID_ARGUMENT | `CreateProjectNoOwnerLikeRoleGrant` | The create project request would create a project with no principal being granted an owner-like role. As a result, there would be no user with administrative privileges over the project. A role is defined to be owner-like if it has the `compass:edit-project` operation. In the common case of the default role-set, this is just the `compass:manage` role. |
| INVALID_ARGUMENT | `OrganizationMarkingNotOnSpace` | At least one of the organization markings associated with a passed organization is not applied on the requested space. |
| PERMISSION_DENIED | `CreateProjectPermissionDenied` | Could not create the Project. |
| NOT_FOUND | `ProjectNotFound` | The given Project could not be found. |
| NOT_FOUND | `SpaceNotFound` | The given Space could not be found. |
