`POST /api/v2/filesystem/folders/getBatch`

Fetches multiple folders in a single request.


The maximum batch size for this endpoint is 1000.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Request body

```json
[
  {
    "folderRid": "ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8"
  }
]
```

## Response

**GetFoldersBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetFoldersBatchResponse` | object | 是 | 示例: `{"data":{"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791":{"projectRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","updatedTime":"2024-09-25T17:29:35.974Z","updatedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","displayName":"My Folder","description":"This dataset contains important data about Empyrean Airlines","rid":"ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8","type":"FOLDER","spaceRid":"ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca","trashStatus":"NOT_TRASHED","parentFolderRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","path":"/Empyrean Airlines/My Important Project/My Folder","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","createdTime":"2024-09-25T17:29:35.974Z"}}}` |
| `GetFoldersBatchResponse.data` | map | 否 | — |
| `GetFoldersBatchResponse.data.FolderRid` | string | 是 | The unique resource identifier (RID) of a Folder. |
| `GetFoldersBatchResponse.data.Folder` | object | 是 | — |
| `GetFoldersBatchResponse.data.Folder.rid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |
| `GetFoldersBatchResponse.data.Folder.displayName` | string | 是 | The display name of the resource<br>示例: `My Folder` |
| `GetFoldersBatchResponse.data.Folder.description` | string | 否 | The description associated with the Folder. |
| `GetFoldersBatchResponse.data.Folder.documentation` | string | 否 | The documentation associated with the Folder. |
| `GetFoldersBatchResponse.data.Folder.path` | string | 是 | The full path to the resource, including the resource name itself<br>示例: `/Empyrean Airlines/My Important Project/My Folder` |
| `GetFoldersBatchResponse.data.Folder.type` | enum | 是 | A folder can be a regular Folder, a<br>[Project](/docs/foundry/getting-started/projects-and-resources/#projects) or a<br>[Space](/docs/foundry/security/orgs-and-spaces/#spaces).<br>示例: `FOLDER` |
| `GetFoldersBatchResponse.data.Folder.createdBy` | string | 是 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `GetFoldersBatchResponse.data.Folder.updatedBy` | string | 是 | The Foundry user who last updated this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `GetFoldersBatchResponse.data.Folder.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2024-09-25T17:29:35.974Z` |
| `GetFoldersBatchResponse.data.Folder.updatedTime` | string | 是 | The time at which the resource was most recently updated.<br>示例: `2024-09-25T17:29:35.974Z` |
| `GetFoldersBatchResponse.data.Folder.trashStatus` | enum | 是 | The trash status of the Folder. If trashed, this could either be because the Folder itself has been<br>trashed or because one of its ancestors has been trashed.<br>示例: `NOT_TRASHED` |
| `GetFoldersBatchResponse.data.Folder.parentFolderRid` | string | 是 | The parent folder Resource Identifier (RID). For Projects, this will be the Space RID and for Spaces,<br>this value will be the root folder (`ri.compass.main.folder.0`).<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `GetFoldersBatchResponse.data.Folder.projectRid` | string | 否 | The Project Resource Identifier (RID) that the Folder lives in. If the Folder is a Space, this value will<br>not be defined.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `GetFoldersBatchResponse.data.Folder.spaceRid` | string | 是 | The Space Resource Identifier (RID) that the Folder lives in. If the Folder is a Space, this value will<br>be the same as the Folder RID.<br>示例: `ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca` |

```json
{
  "data": {
    "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791": {
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
  }
}
```
