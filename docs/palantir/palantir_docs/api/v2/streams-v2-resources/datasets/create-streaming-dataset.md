`POST /api/v2/streams/datasets/create`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a streaming dataset with a stream on the specified branch, or if no branch is specified, on the
default branch ('master' for most enrollments). For more information on streaming datasets, refer to the
[streams](/docs/foundry/data-integration/streams/) user documentation.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:streams-write`.

**OAuth2 scopes**: `api:streams-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "parentFolderRid": "ri.compass.main.folder.c410f510-2937-420e-8ea3-8c9bcb3c1791",
  "schema": {
    "fields": [
      {
        "name": "timestamp",
        "schema": {
          "nullable": false,
          "dataType": {
            "type": "timestamp"
          }
        }
      },
      {
        "name": "value",
        "schema": {
          "nullable": false,
          "dataType": {
            "type": "string"
          }
        }
      }
    ],
    "keyFieldNames": [
      "timestamp"
    ]
  },
  "partitionsCount": 1,
  "streamType": "LOW_LATENCY",
  "name": "My Dataset",
  "branchName": "master",
  "compressed": false
}
```

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
| INVALID_ARGUMENT | `InvalidSchema` | The schema failed validations |
| INVALID_ARGUMENT | `InvalidFieldSchema` | The field schema failed validations |
| INVALID_ARGUMENT | `CannotCreateStreamingDatasetInUserFolder` | Cannot create a streaming dataset in a user folder. |
| INVALID_ARGUMENT | `InvalidStreamType` | The stream type is invalid. |
| PERMISSION_DENIED | `CreateStreamingDatasetPermissionDenied` | Could not create the Dataset. |
