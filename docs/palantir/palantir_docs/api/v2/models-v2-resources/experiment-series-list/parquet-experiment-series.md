`GET /api/v2/models/{modelRid}/experiments/{experimentRid}/series/{experimentSeriesName}/parquet`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieve raw time-series data for a single series as a streamed binary response in Apache Parquet format.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `experimentRid` | string | 是 | The Resource Identifier (RID) of an Experiment.<br>示例: `ri.models.main.experiment.abc123` |
| `experimentSeriesName` | string | 是 | The name of a series (metrics tracked over time).<br>示例: `loss` |

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
| NOT_FOUND | `ExperimentSeriesNotFound` | The requested series was not found in the experiment. |
| NOT_FOUND | `ModelExperimentNotFound` | The requested experiment was not found or the user lacks permission to access it. |
| PERMISSION_DENIED | `ParquetExperimentSeriesPermissionDenied` | Could not parquet the ExperimentSeries. |
