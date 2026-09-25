`GET /api/v2/models/{modelRid}/experiments/{experimentRid}/series/{experimentSeriesName}/json`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieve raw time-series data for a single series in JSON format.
Results are paginated with a default page size of 200 and a maximum of 1000.


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
| `pageSize` | integer | 否 | Maximum number of values to return per page. Default is 200, maximum is 1000. |
| `offset` | integer | 否 | Number of values to skip from the beginning. Defaults to 0. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Series**

A series of values logged over time.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Series` | union | 是 | A series of values logged over time.<br>示例: `{"type":"doubleV1","series":[{"value":0.42,"timestamp":1715212800000,"step":1}]}` |
| `Series.doubleV1` | object | 否 | A series of double values. |
| `Series.doubleV1.series` | list<DoubleSeriesValueV1> | 否 | — |
| `Series.doubleV1.series.DoubleSeriesValueV1` | object | 是 | A single double value in a series. |
| `Series.doubleV1.series.DoubleSeriesValueV1.value` | number | 是 | — |
| `Series.doubleV1.series.DoubleSeriesValueV1.timestamp` | string | 是 | Milliseconds since unix time zero<br>示例: `1715212800000` |
| `Series.doubleV1.series.DoubleSeriesValueV1.step` | string | 是 | — |

```json
{
  "type": "doubleV1",
  "series": [
    {
      "value": 0.42,
      "timestamp": 1715212800000,
      "step": 1
    }
  ]
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ExperimentSeriesNotFound` | The requested series was not found in the experiment. |
| NOT_FOUND | `ModelExperimentNotFound` | The requested experiment was not found or the user lacks permission to access it. |
| PERMISSION_DENIED | `JsonExperimentSeriesPermissionDenied` | Could not json the ExperimentSeries. |
