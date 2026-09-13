`GET /api/v2/filesystem/folders/{folderRid}`

Get the Folder with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `folderRid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |

## Response

**Folder**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Folder` | object | 是 | 示例: `{"projectRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","updatedTime":"2024-09-25T17:29:35.974Z","updatedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","displayName":"My Folder","description":"This dataset contains important data about Empyrean Airlines","rid":"ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8","type":"FOLDER","spaceRid":"ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca","trashStatus":"NOT_TRASHED","parentFolderRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","path":"/Empyrean Airlines/My Important Project/My Folder","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","createdTime":"2024-09-25T17:29:35.974Z"}` |
| `Folder.rid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |
| `Folder.displayName` | string | 是 | The display name of the resource<br>示例: `My Folder` |
| `Folder.description` | string | 否 | The description associated with the Folder. |
| `Folder.documentation` | string | 否 | The documentation associated with the Folder. |
| `Folder.path` | string | 是 | The full path to the resource, including the resource name itself<br>示例: `/Empyrean Airlines/My Important Project/My Folder` |
| `Folder.type` | enum | 是 | A folder can be a regular Folder, a<br>[Project](/docs/foundry/getting-started/projects-and-resources/#projects) or a<br>[Space](/docs/foundry/security/orgs-and-spaces/#spaces).<br>示例: `FOLDER` |
| `Folder.createdBy` | string | 是 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Folder.updatedBy` | string | 是 | The Foundry user who last updated this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Folder.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Folder.updatedTime` | string | 是 | The time at which the resource was most recently updated.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Folder.trashStatus` | enum | 是 | The trash status of the Folder. If trashed, this could either be because the Folder itself has been<br>trashed or because one of its ancestors has been trashed.<br>示例: `NOT_TRASHED` |
| `Folder.parentFolderRid` | string | 是 | The parent folder Resource Identifier (RID). For Projects, this will be the Space RID and for Spaces,<br>this value will be the root folder (`ri.compass.main.folder.0`).<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `Folder.projectRid` | string | 否 | The Project Resource Identifier (RID) that the Folder lives in. If the Folder is a Space, this value will<br>not be defined.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `Folder.spaceRid` | string | 是 | The Space Resource Identifier (RID) that the Folder lives in. If the Folder is a Space, this value will<br>be the same as the Folder RID.<br>示例: `ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca` |

```json
{
  "projectRid": "ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33",
  "updatedTime": "2024-09-25T17:29:35.974Z",
  "updatedBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "displayName": "My Folder",
  "description": "This dataset contains important data about Empyrean Airlines",
  "rid": "ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8",
  "type": "FOLDER",
  "spaceRid": "ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca",
  "trashStatus": "NOT_TRASHED",
  "parentFolderRid": "ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33",
  "path": "/Empyrean Airlines/My Important Project/My Folder",
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "createdTime": "2024-09-25T17:29:35.974Z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidFolder` | The given resource is not a Folder. |
| INVALID_ARGUMENT | `GetRootFolderNotSupported` | Getting the root folder as a resource is not supported. |
| NOT_FOUND | `FolderNotFound` | The given Folder could not be found. |
