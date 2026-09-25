`GET /api/v2/filesystem/projects/{projectRid}`

Get the Project with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `projectRid` | string | 是 | The unique resource identifier (RID) of a Project.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |

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
| NOT_FOUND | `ProjectNotFound` | The given Project could not be found. |
