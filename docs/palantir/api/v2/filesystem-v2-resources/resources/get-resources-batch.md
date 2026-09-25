`POST /api/v2/filesystem/resources/getBatch`

Fetches multiple resources in a single request.
Returns a map from RID to the corresponding resource. If a resource does not exist, or if it is a root folder or space, its RID will not be included in the map.
At most 1,000 resources should be requested at once.


The maximum batch size for this endpoint is 1000.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:filesystem-read`.

**OAuth2 scopes**: `api:filesystem-read`

## Request body

```json
[
  {
    "resourceRid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da"
  }
]
```

## Response

**GetResourcesBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetResourcesBatchResponse` | object | 是 | — |
| `GetResourcesBatchResponse.data` | map | 否 | — |
| `GetResourcesBatchResponse.data.ResourceRid` | string | 是 | The unique resource identifier (RID) of a resource. |
| `GetResourcesBatchResponse.data.Resource` | object | 是 | — |
| `GetResourcesBatchResponse.data.Resource.rid` | string | 是 | The unique resource identifier (RID) of a resource.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `GetResourcesBatchResponse.data.Resource.displayName` | string | 是 | The display name of the resource<br>示例: `My Dataset` |
| `GetResourcesBatchResponse.data.Resource.description` | string | 否 | The description of the resource<br>示例: `This dataset contains important data about Empyrean Airlines` |
| `GetResourcesBatchResponse.data.Resource.documentation` | string | 否 | The documentation associated with the resource |
| `GetResourcesBatchResponse.data.Resource.path` | string | 是 | The full path to the resource, including the resource name itself<br>示例: `/Empyrean Airlines/My Important Project/My Dataset` |
| `GetResourcesBatchResponse.data.Resource.type` | enum | 是 | The type of the resource derived from the Resource Identifier (RID).<br>示例: `FOUNDRY_DATASET` |
| `GetResourcesBatchResponse.data.Resource.createdBy` | string | 是 | The user that created the resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `GetResourcesBatchResponse.data.Resource.updatedBy` | string | 是 | The user that last updated the resource.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `GetResourcesBatchResponse.data.Resource.createdTime` | string | 是 | The timestamp that the resource was last created.<br>示例: `2024-09-25T17:29:35.974Z` |
| `GetResourcesBatchResponse.data.Resource.updatedTime` | string | 是 | The timestamp that the resource was last modified. For folders, this includes any of its descendants. For<br>top level folders (spaces and projects), this is not updated by child updates for performance reasons.<br>示例: `2024-09-25T17:29:35.974Z` |
| `GetResourcesBatchResponse.data.Resource.trashStatus` | enum | 是 | The trash status of the resource. If trashed, this could either be because the resource itself has been<br>trashed or because one of its ancestors has been trashed.<br>示例: `NOT_TRASHED` |
| `GetResourcesBatchResponse.data.Resource.parentFolderRid` | string | 是 | The parent folder Resource Identifier (RID). For projects, this will be the Space RID.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `GetResourcesBatchResponse.data.Resource.projectRid` | string | 是 | The Project Resource Identifier (RID) that the resource lives in. If the resource itself is a<br>Project, this value will still be populated with the Project RID.<br>示例: `ri.compass.main.folder.4cae7c13-b59f-48f6-9ef2-dbde603e4e33` |
| `GetResourcesBatchResponse.data.Resource.spaceRid` | string | 是 | The Space Resource Identifier (RID) that the resource lives in.<br>示例: `ri.compass.main.folder.a86ad5f5-3db5-48e4-9fdd-00aa3e5731ca` |
