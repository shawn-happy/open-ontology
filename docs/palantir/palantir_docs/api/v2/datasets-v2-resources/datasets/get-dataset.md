`GET /api/v2/datasets/{datasetRid}`

Get the Dataset with the specified rid.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:datasets-read`.

**OAuth2 scopes**: `api:datasets-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |

## Response

**Dataset**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Dataset` | object | 是 | 示例: `{"parentFolderRid":"ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791","name":"My Dataset","rid":"ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da"}` |
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
| NOT_FOUND | `DatasetNotFound` | The requested dataset could not be found, or the client token does not have access to it. |
