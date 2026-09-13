`GET /api/v2/models/modelStudios/{modelStudioRid}/configVersions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Lists all configuration versions for a Model Studio.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelStudioRid` | string | 是 | The Resource Identifier (RID) of a Model Studio.<br>示例: `ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListModelStudioConfigVersionsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListModelStudioConfigVersionsResponse` | object | 是 | 示例: `{"data":[{"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","trainer":{"trainerId":"ri.models..trainer.autogluon_tabular_regression","version":"0.388.0"},"name":"Initial configuration","resources":{"memory":"4G","cpu":"2"},"createdTime":"2003-05-06T12:34:56.789Z","version":1,"workerConfig":{"outputs":{"model":{"type":"model","modelRid":"ri.models.main.model.a1b2c3d4-e5f6-7890-abcd-ef1234567890"}},"inputs":{"input_df":{"type":"dataset","rid":"ri.foundry.main.dataset.a1b2c3d4-e5f6-7890-abcd-ef1234567890","columnMapping":{"target_column":["target"]},"ignoreColumns":[],"selectColumns":[]}}},"trainerId":"ri.models..trainer.autogluon_tabular_regression"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListModelStudioConfigVersionsResponse.data` | list<ModelStudioConfigVersion> | 否 | — |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion` | object | 是 | — |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.name` | string | 是 | Human readable name of the configuration version and experiment.<br>示例: `Initial configuration` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.version` | integer | 是 | The version number of this configuration.<br>示例: `1` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.trainerId` | string | 是 | The identifier of the trainer to use for this configuration.<br>示例: `ri.models..trainer.autogluon_tabular_regression` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.trainer` | object | 是 | The trainer and version used for this configuration.<br>示例: `{"trainerId":"ri.models..trainer.autogluon_tabular_regression","version":"0.388.0"}` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.trainer.trainerId` | string | 是 | The Resource Identifier (RID) of a trainer.<br>示例: `ri.models..trainer.autogluon_tabular_regression` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.trainer.version` | string | 是 | — |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig` | object | 是 | The worker configuration including inputs, outputs, and custom settings.<br>示例: `{"outputs":{"model":{"type":"model","modelRid":"ri.models.main.model.a1b2c3d4-e5f6-7890-abcd-ef1234567890"}},"inputs":{"input_df":{"type":"dataset","rid":"ri.foundry.main.dataset.a1b2c3d4-e5f6-7890-abcd-ef1234567890","columnMapping":{"target_column":["target"]},"ignoreColumns":[],"selectColumns":[]}}}` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.customConfig` | map | 否 | Custom configuration matching the trainer's JSON schema. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs` | map | 否 | Input configurations keyed by alias. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.InputAlias` | string | 是 | A string alias used to identify inputs in a Model Studio configuration. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput` | union | 是 | Input specification for a Model Studio configuration. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset` | object | 否 | Dataset input configuration. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.rid` | string | 是 | The RID of the input dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.columnMapping` | map | 否 | Mapping of column type spec IDs to column names. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.columnMapping.ColumnTypeSpecId` | string | 是 | An identifier for a column type specification. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.columnMapping.array` | list<ColumnName> | 是 | — |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.columnMapping.array.ColumnName` | string | 是 | The name of a column in a dataset. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.ignoreColumns` | list<ColumnName> | 否 | Columns to ignore from the dataset. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.ignoreColumns.ColumnName` | string | 是 | The name of a column in a dataset. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.selectColumns` | list<ColumnName> | 否 | Columns to select from the dataset. If empty, all columns not in ignoreColumns will be used. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.inputs.ModelStudioInput.dataset.selectColumns.ColumnName` | string | 是 | The name of a column in a dataset. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.outputs` | map | 否 | Output configurations keyed by alias. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.outputs.OutputAlias` | string | 是 | A string alias used to identify outputs in a Model Studio configuration. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.outputs.ModelStudioOutput` | union | 是 | Output specification for a Model Studio configuration. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.outputs.ModelStudioOutput.model` | object | 否 | Model output configuration. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.workerConfig.outputs.ModelStudioOutput.model.modelRid` | string | 是 | The RID of the output model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.resources` | object | 是 | The compute resources allocated for training runs.<br>示例: `{"memory":"4G","cpu":"2"}` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.resources.memory` | string | 是 | Memory allocation (e.g., "4G"). |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.resources.cpu` | string | 是 | CPU allocation (e.g., "2"). |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.resources.gpu` | enum | 否 | GPU allocation (must be available in the project's resource queue).<br>示例: `A100` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.changelog` | string | 否 | Changelog describing changes in this version. |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.createdBy` | string | 是 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListModelStudioConfigVersionsResponse.data.ModelStudioConfigVersion.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListModelStudioConfigVersionsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "trainer": {
        "trainerId": "ri.models..trainer.autogluon_tabular_regression",
        "version": "0.388.0"
      },
      "name": "Initial configuration",
      "resources": {
        "memory": "4G",
        "cpu": "2"
      },
      "createdTime": "2003-05-06T12:34:56.789Z",
      "version": 1,
      "workerConfig": {
        "outputs": {
          "model": {
            "type": "model",
            "modelRid": "ri.models.main.model.a1b2c3d4-e5f6-7890-abcd-ef1234567890"
          }
        },
        "inputs": {
          "input_df": {
            "type": "dataset",
            "rid": "ri.foundry.main.dataset.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
            "columnMapping": {
              "target_column": [
                "target"
              ]
            },
            "ignoreColumns": [],
            "selectColumns": []
          }
        }
      },
      "trainerId": "ri.models..trainer.autogluon_tabular_regression"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ModelStudioNotFound` | The requested Model Studio was not found. |
