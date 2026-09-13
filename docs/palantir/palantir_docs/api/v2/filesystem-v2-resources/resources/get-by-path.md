`GET /api/v2/filesystem/resources/getByPath`

Get a resource by its absolute path.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `path` | string | 是 | The path to the resource. The leading slash is optional.<br>示例: `/My Organization-abcd/My Important Project/My Dataset` |

## Response

**Resource**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Resource` | object | 是 | 示例: `{"projectRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","updatedTime":"2024-09-25T17:29:35.974Z","updatedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","displayName":"My Dataset","description":"This dataset contains important data about Empyrean Airlines","rid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","type":"FOUNDRY_DATASET","spaceRid":"ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca","trashStatus":"NOT_TRASHED","parentFolderRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","path":"/Empyrean Airlines/My Important Project/My Dataset","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","createdTime":"2024-09-25T17:29:35.974Z"}` |
| `Resource.rid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Resource.displayName` | string | 是 | The display name of the resource<br>示例: `My Dataset` |
| `Resource.description` | string | 否 | The description of the resource<br>示例: `This dataset contains important data about Empyrean Airlines` |
| `Resource.documentation` | string | 否 | The documentation associated with the resource |
| `Resource.path` | string | 是 | The full path to the resource, including the resource name itself<br>示例: `/Empyrean Airlines/My Important Project/My Dataset` |
| `Resource.type` | enum | 是 | The type of the resource derived from the Resource Identifier (RID).<br>示例: `FOUNDRY_DATASET` |
| `Resource.createdBy` | string | 是 | The user that created the resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Resource.updatedBy` | string | 是 | The user that last updated the resource.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Resource.createdTime` | string | 是 | The timestamp that the resource was last created.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Resource.updatedTime` | string | 是 | The timestamp that the resource was last modified. For folders, this includes any of its descendants. For<br>top level folders (spaces and projects), this is not updated by child updates for performance reasons.<br>示例: `2024-09-25T17:29:35.974Z` |
| `Resource.trashStatus` | enum | 是 | The trash status of the resource. If trashed, this could either be because the resource itself has been<br>trashed or because one of its ancestors has been trashed.<br>示例: `NOT_TRASHED` |
| `Resource.parentFolderRid` | string | 是 | The parent folder Resource Identifier (RID). For projects, this will be the Space RID.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `Resource.projectRid` | string | 是 | The Project Resource Identifier (RID) that the resource lives in. If the resource itself is a<br>Project, this value will still be populated with the Project RID.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `Resource.spaceRid` | string | 是 | The Space Resource Identifier (RID) that the resource lives in.<br>示例: `ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca` |

```json
{
  "projectRid": "ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33",
  "updatedTime": "2024-09-25T17:29:35.974Z",
  "updatedBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "displayName": "My Dataset",
  "description": "This dataset contains important data about Empyrean Airlines",
  "rid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da",
  "type": "FOUNDRY_DATASET",
  "spaceRid": "ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca",
  "trashStatus": "NOT_TRASHED",
  "parentFolderRid": "ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33",
  "path": "/Empyrean Airlines/My Important Project/My Dataset",
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "createdTime": "2024-09-25T17:29:35.974Z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `PathNotFound` | The given path could not be found. |
| INVALID_ARGUMENT | `GetRootFolderNotSupported` | Getting the root folder as a resource is not supported. |
| INVALID_ARGUMENT | `GetSpaceResourceNotSupported` | Getting a space as a resource is not supported. |
| INVALID_ARGUMENT | `InvalidPath` | The given path is invalid.<br>A valid path has all components separated by a single `/`. |
| PERMISSION_DENIED | `GetByPathPermissionDenied` | Could not getByPath the Resource. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
