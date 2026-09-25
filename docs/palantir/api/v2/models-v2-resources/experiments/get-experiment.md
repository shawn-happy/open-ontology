`GET /api/v2/models/{modelRid}/experiments/{experimentRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieve a single experiment with all metadata, parameters, series metadata, and summary metrics.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `experimentRid` | string | 是 | The Resource Identifier (RID) of an Experiment.<br>示例: `ri.models.main.experiment.abc123` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Experiment**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Experiment` | object | 是 | 示例: `{"source":{"type":"codeWorkspace","containerRid":"ri.foundry-container-service.main.container.a1b2c3d4-e5f6-7890-abcd-ef1234567890","deploymentRid":"ri.foundry-container-service.main.deployment.b2c3d4e5-f6a7-8901-bcde-f12345678901"},"rid":"ri.models.main.experiment.abc123","branch":"master","summaryMetrics":[{"seriesName":"loss","aggregation":"LAST","value":0.07}],"tags":["production"],"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","series":[{"name":"loss","length":100,"value":{"type":"double","min":0.05,"max":1.5,"last":0.07}}],"linkedModelVersion":"ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee","createdTime":"2003-05-06T12:34:56.789Z","jobRid":"ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448","parameters":[{"name":"learning_rate","value":{"type":"double","value":0.001}}],"modelRid":"ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9","status":"RUNNING","artifacts":{"predictions_table":{"name":"predictions_table","description":"Test set predictions","sizeBytes":4096,"details":{"type":"table","rowCount":100}}}}` |
| `Experiment.rid` | string | 是 | The Resource Identifier (RID) of an Experiment.<br>示例: `ri.models.main.experiment.abc123` |
| `Experiment.modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `Experiment.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `Experiment.createdBy` | string | 是 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `Experiment.source` | union | 是 | The source from which the experiment was created.<br>示例: `{"type":"codeWorkspace","containerRid":"ri.foundry-container-service.main.container.a1b2c3d4-e5f6-7890-abcd-ef1234567890","deploymentRid":"ri.foundry-container-service.main.deployment.b2c3d4e5-f6a7-8901-bcde-f12345678901"}` |
| `Experiment.source.codeWorkspace` | object | 否 | Experiment created from a code workspace. |
| `Experiment.source.codeWorkspace.containerRid` | string | 是 | — |
| `Experiment.source.codeWorkspace.deploymentRid` | string | 否 | — |
| `Experiment.source.authoring` | object | 否 | Experiment created from an authoring repository. |
| `Experiment.source.authoring.stemmaRid` | string | 是 | — |
| `Experiment.source.sdk` | object | 否 | Experiment created from the SDK. |
| `Experiment.status` | enum | 是 | The current status of an experiment.<br>示例: `RUNNING` |
| `Experiment.statusMessage` | string | 否 | — |
| `Experiment.branch` | string | 是 | The name of a Branch.<br>示例: `master` |
| `Experiment.parameters` | list<Parameter> | 否 | — |
| `Experiment.parameters.Parameter` | object | 是 | A parameter with its name and value. |
| `Experiment.parameters.Parameter.name` | string | 是 | The parameter name<br>示例: `learning_rate` |
| `Experiment.parameters.Parameter.value` | union | 是 | The parameter value<br>示例: `{"type":"double","value":0.001}` |
| `Experiment.parameters.Parameter.value.datetime` | object | 否 | A datetime parameter value. |
| `Experiment.parameters.Parameter.value.datetime.value` | string | 是 | — |
| `Experiment.parameters.Parameter.value.boolean` | object | 否 | A boolean parameter value. |
| `Experiment.parameters.Parameter.value.boolean.value` | boolean | 是 | — |
| `Experiment.parameters.Parameter.value.string` | object | 否 | A string parameter value. |
| `Experiment.parameters.Parameter.value.string.value` | string | 是 | — |
| `Experiment.parameters.Parameter.value.double` | object | 否 | A double parameter value. |
| `Experiment.parameters.Parameter.value.double.value` | number | 是 | — |
| `Experiment.parameters.Parameter.value.integer` | object | 否 | An integer parameter value. |
| `Experiment.parameters.Parameter.value.integer.value` | string | 是 | — |
| `Experiment.series` | list<SeriesAggregations> | 否 | — |
| `Experiment.series.SeriesAggregations` | object | 是 | Series with precomputed aggregation values. |
| `Experiment.series.SeriesAggregations.name` | string | 是 | The series name<br>示例: `loss` |
| `Experiment.series.SeriesAggregations.length` | string | 否 | Number of values in the series. This field may be absent when series aggregations are derived from summary metrics rather than the full series data. |
| `Experiment.series.SeriesAggregations.value` | union | 是 | Aggregated values for this series<br>示例: `{"type":"double","min":0.05,"max":1.5,"last":0.07}` |
| `Experiment.series.SeriesAggregations.value.double` | object | 否 | Aggregated statistics for numeric series. |
| `Experiment.series.SeriesAggregations.value.double.min` | number | 否 | Minimum value in the series. Absent if the metric has not been computed. |
| `Experiment.series.SeriesAggregations.value.double.max` | number | 否 | Maximum value in the series. Absent if the metric has not been computed. |
| `Experiment.series.SeriesAggregations.value.double.last` | number | 否 | Most recent value in the series. Absent if the metric has not been computed. |
| `Experiment.summaryMetrics` | list<SummaryMetric> | 否 | — |
| `Experiment.summaryMetrics.SummaryMetric` | object | 是 | A summary metric with series name, aggregation type, and computed value. |
| `Experiment.summaryMetrics.SummaryMetric.seriesName` | string | 是 | Name of the series this metric belongs to<br>示例: `loss` |
| `Experiment.summaryMetrics.SummaryMetric.aggregation` | enum | 是 | Type of aggregation (MIN, MAX, LAST)<br>示例: `MIN` |
| `Experiment.summaryMetrics.SummaryMetric.value` | number | 是 | The computed value |
| `Experiment.artifacts` | map | 否 | — |
| `Experiment.artifacts.ExperimentArtifactName` | string | 是 | The name of an experiment artifact. |
| `Experiment.artifacts.ExperimentArtifactMetadata` | object | 是 | Metadata about an experiment artifact. |
| `Experiment.artifacts.ExperimentArtifactMetadata.name` | string | 是 | The name of an experiment artifact.<br>示例: `predictions_table` |
| `Experiment.artifacts.ExperimentArtifactMetadata.description` | string | 否 | — |
| `Experiment.artifacts.ExperimentArtifactMetadata.sizeBytes` | string | 是 | The size of the file or attachment in bytes.<br>示例: `72526847` |
| `Experiment.artifacts.ExperimentArtifactMetadata.details` | union | 是 | Details about an experiment artifact.<br>示例: `{"type":"table","rowCount":100}` |
| `Experiment.artifacts.ExperimentArtifactMetadata.details.table` | object | 否 | Details about a table artifact. |
| `Experiment.artifacts.ExperimentArtifactMetadata.details.table.rowCount` | string | 是 | — |
| `Experiment.tags` | list<ExperimentTagText> | 否 | — |
| `Experiment.tags.ExperimentTagText` | string | 是 | A tag associated with an experiment. |
| `Experiment.linkedModelVersion` | string | 否 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `Experiment.jobRid` | string | 否 | The RID of a Job.<br>示例: `ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448` |

```json
{
  "source": {
    "type": "codeWorkspace",
    "containerRid": "ri.foundry-container-service.main.container.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
    "deploymentRid": "ri.foundry-container-service.main.deployment.b2c3d4e5-f6a7-8901-bcde-f12345678901"
  },
  "rid": "ri.models.main.experiment.abc123",
  "branch": "master",
  "summaryMetrics": [
    {
      "seriesName": "loss",
      "aggregation": "LAST",
      "value": 0.07
    }
  ],
  "tags": [
    "production"
  ],
  "createdBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
  "series": [
    {
      "name": "loss",
      "length": 100,
      "value": {
        "type": "double",
        "min": 0.05,
        "max": 1.5,
        "last": 0.07
      }
    }
  ],
  "linkedModelVersion": "ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee",
  "createdTime": "2003-05-06T12:34:56.789Z",
  "jobRid": "ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448",
  "parameters": [
    {
      "name": "learning_rate",
      "value": {
        "type": "double",
        "value": 0.001
      }
    }
  ],
  "modelRid": "ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9",
  "status": "RUNNING",
  "artifacts": {
    "predictions_table": {
      "name": "predictions_table",
      "description": "Test set predictions",
      "sizeBytes": 4096,
      "details": {
        "type": "table",
        "rowCount": 100
      }
    }
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ModelExperimentNotFound` | The requested experiment was not found or the user lacks permission to access it. |
| NOT_FOUND | `ExperimentNotFound` | The given Experiment could not be found. |
