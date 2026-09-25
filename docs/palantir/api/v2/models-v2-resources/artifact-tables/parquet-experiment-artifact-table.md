`GET /api/v2/models/{modelRid}/experiments/{experimentRid}/artifactTables/{experimentArtifactTableName}/parquet`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Read raw table data from experiment artifacts in Parquet format.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `experimentRid` | string | 是 | The Resource Identifier (RID) of an Experiment.<br>示例: `ri.models.main.experiment.abc123` |
| `experimentArtifactTableName` | string | 是 | The name of an experiment artifact.<br>示例: `predictions_table` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**body**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `body` | string | 是 | — |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ExperimentArtifactNotFound` | The requested artifact was not found in the experiment. |
| NOT_FOUND | `ModelExperimentNotFound` | The requested experiment was not found or the user lacks permission to access it. |
| PERMISSION_DENIED | `ParquetExperimentArtifactTablePermissionDenied` | Could not parquet the ExperimentArtifactTable. |
