`POST /api/v2/datasets`

Creates a new Dataset. A default branch - `master` for most enrollments - will be created on the Dataset.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-write`.

**OAuth2 scopes**: `api:datasets-write`

## Request body

```json
{
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "name": "My Dataset"
}
```

## Response

**Dataset**

The created Dataset

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Dataset` | object | 是 | The created Dataset<br>示例: `{"parentFolderRid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791","name":"My Dataset","rid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da"}` |
| `Dataset.rid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `Dataset.name` | string | 是 | 示例: `My Dataset` |
| `Dataset.parentFolderRid` | string | 是 | The unique resource identifier (RID) of a Folder.<br>示例: `ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791` |

```json
{
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "name": "My Dataset",
  "rid": "ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| CONFLICT | `ResourceNameAlreadyExists` | The provided resource name is already in use by another resource in the same folder. |
| PERMISSION_DENIED | `CreateDatasetPermissionDenied` | The provided token does not have permission to create a dataset in this folder. |
| INVALID_ARGUMENT | `TransactionNotCommitted` | The given transaction has not been committed. |
| NOT_FOUND | `TransactionNotFound` | The requested transaction could not be found on the dataset, or the client token does not have access to it. |
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
| PERMISSION_DENIED | `CreateBranchPermissionDenied` | The provided token does not have permission to create a branch of this dataset. |
| CONFLICT | `BranchAlreadyExists` | The branch cannot be created because a branch with that name already exists. |
| INVALID_ARGUMENT | `InvalidBranchName` | The requested branch name cannot be used. Branch names cannot be empty and must not look like RIDs or UUIDs. |
| INVALID_ARGUMENT | `InvalidDisplayName` | The display name of a resource should not be exactly `.` or `..`, contain a forward slash `/` and must be<br>less than or equal to 700 characters. |
| NOT_FOUND | `FolderNotFound` | The given Folder could not be found. |
