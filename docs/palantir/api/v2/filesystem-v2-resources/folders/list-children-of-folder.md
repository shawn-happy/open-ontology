`GET /api/v2/filesystem/folders/{folderRid}/children`

List all child resources of the Folder.

This is a paged endpoint. The page size will be limited to 2,000 results per page. If no page size is
provided, this page size will also be used as the default.

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `folderRid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.01a79a9d-e293-48db-a585-9ffe221536e8` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListChildrenOfFolderResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListChildrenOfFolderResponse` | object | 是 | 示例: `{"data":[{"projectRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","updatedTime":"2024-09-25T17:29:35.974Z","updatedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","displayName":"My Dataset","description":"This dataset contains important data about Empyrean Airlines","rid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da","type":"FOUNDRY_DATASET","spaceRid":"ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca","trashStatus":"NOT_TRASHED","parentFolderRid":"ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33","path":"/Empyrean Airlines/My Important Project/My Dataset","createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","createdTime":"2024-09-25T17:29:35.974Z"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListChildrenOfFolderResponse.data` | list<Resource> | 否 | — |
| `ListChildrenOfFolderResponse.data.Resource` | object | 是 | — |
| `ListChildrenOfFolderResponse.data.Resource.rid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `ListChildrenOfFolderResponse.data.Resource.displayName` | string | 是 | The display name of the resource<br>示例: `My Dataset` |
| `ListChildrenOfFolderResponse.data.Resource.description` | string | 否 | The description of the resource<br>示例: `This dataset contains important data about Empyrean Airlines` |
| `ListChildrenOfFolderResponse.data.Resource.documentation` | string | 否 | The documentation associated with the resource |
| `ListChildrenOfFolderResponse.data.Resource.path` | string | 是 | The full path to the resource, including the resource name itself<br>示例: `/Empyrean Airlines/My Important Project/My Dataset` |
| `ListChildrenOfFolderResponse.data.Resource.type` | enum | 是 | The type of the resource derived from the Resource Identifier (RID).<br>示例: `FOUNDRY_DATASET` |
| `ListChildrenOfFolderResponse.data.Resource.createdBy` | string | 是 | The user that created the resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListChildrenOfFolderResponse.data.Resource.updatedBy` | string | 是 | The user that last updated the resource.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListChildrenOfFolderResponse.data.Resource.createdTime` | string | 是 | The timestamp that the resource was last created.<br>示例: `2024-09-25T17:29:35.974Z` |
| `ListChildrenOfFolderResponse.data.Resource.updatedTime` | string | 是 | The timestamp that the resource was last modified. For folders, this includes any of its descendants. For<br>top level folders (spaces and projects), this is not updated by child updates for performance reasons.<br>示例: `2024-09-25T17:29:35.974Z` |
| `ListChildrenOfFolderResponse.data.Resource.trashStatus` | enum | 是 | The trash status of the resource. If trashed, this could either be because the resource itself has been<br>trashed or because one of its ancestors has been trashed.<br>示例: `NOT_TRASHED` |
| `ListChildrenOfFolderResponse.data.Resource.parentFolderRid` | string | 是 | The parent folder Resource Identifier (RID). For projects, this will be the Space RID.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `ListChildrenOfFolderResponse.data.Resource.projectRid` | string | 是 | The Project Resource Identifier (RID) that the resource lives in. If the resource itself is a<br>Project, this value will still be populated with the Project RID.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `ListChildrenOfFolderResponse.data.Resource.spaceRid` | string | 是 | The Space Resource Identifier (RID) that the resource lives in.<br>示例: `ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca` |
| `ListChildrenOfFolderResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
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
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidFolder` | The given resource is not a Folder. |
| INVALID_ARGUMENT | `GetRootFolderNotSupported` | Getting the root folder as a resource is not supported. |
| INVALID_ARGUMENT | `GetSpaceResourceNotSupported` | Getting a space as a resource is not supported. |
| NOT_FOUND | `FolderNotFound` | The given Folder could not be found. |
| NOT_FOUND | `ResourceNotFound` | The given Resource could not be found. |
