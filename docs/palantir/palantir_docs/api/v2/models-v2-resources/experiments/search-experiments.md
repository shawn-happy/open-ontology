`POST /api/v2/models/{modelRid}/experiments/search`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Search experiments using complex nested queries on experiment metadata, parameters, series,
and summary metrics. Supports AND/OR/NOT combinations and various predicates.
Returns a maximum of 100 results per page.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "orderBy": {
    "field": "EXPERIMENT_NAME",
    "direction": "ASC"
  },
  "pageSize": 100,
  "pageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Response

**SearchExperimentsResponse**

Response from searching experiments.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `SearchExperimentsResponse` | object | 是 | Response from searching experiments.<br>示例: `{"data":[{"source":{"type":"codeWorkspace","containerRid":"ri.foundry-container-service.main.container.a1b2c3d4-e5f6-7890-abcd-ef1234567890","deploymentRid":"ri.foundry-container-service.main.deployment.b2c3d4e5-f6a7-8901-bcde-f12345678901"},"rid":"ri.models.main.experiment.abc123","branch":"master","summaryMetrics":[{"seriesName":"loss","aggregation":"LAST","value":0.07}],"tags":["production"],"createdBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","series":[{"name":"loss","length":100,"value":{"type":"double","min":0.05,"max":1.5,"last":0.07}}],"linkedModelVersion":"ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee","createdTime":"2003-05-06T12:34:56.789Z","jobRid":"ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448","parameters":[{"name":"learning_rate","value":{"type":"double","value":0.001}}],"modelRid":"ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9","status":"RUNNING","artifacts":{"predictions_table":{"name":"predictions_table","description":"Test set predictions","sizeBytes":4096,"details":{"type":"table","rowCount":100}}}}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `SearchExperimentsResponse.data` | list<Experiment> | 否 | List of experiments matching the search criteria. |
| `SearchExperimentsResponse.data.Experiment` | object | 是 | — |
| `SearchExperimentsResponse.data.Experiment.rid` | string | 是 | The Resource Identifier (RID) of an Experiment.<br>示例: `ri.models.main.experiment.abc123` |
| `SearchExperimentsResponse.data.Experiment.modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `SearchExperimentsResponse.data.Experiment.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `SearchExperimentsResponse.data.Experiment.createdBy` | string | 是 | The Foundry user who created this resource<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `SearchExperimentsResponse.data.Experiment.source` | union | 是 | The source from which the experiment was created.<br>示例: `{"type":"codeWorkspace","containerRid":"ri.foundry-container-service.main.container.a1b2c3d4-e5f6-7890-abcd-ef1234567890","deploymentRid":"ri.foundry-container-service.main.deployment.b2c3d4e5-f6a7-8901-bcde-f12345678901"}` |
| `SearchExperimentsResponse.data.Experiment.source.codeWorkspace` | object | 否 | Experiment created from a code workspace. |
| `SearchExperimentsResponse.data.Experiment.source.codeWorkspace.containerRid` | string | 是 | — |
| `SearchExperimentsResponse.data.Experiment.source.codeWorkspace.deploymentRid` | string | 否 | — |
| `SearchExperimentsResponse.data.Experiment.source.authoring` | object | 否 | Experiment created from an authoring repository. |
| `SearchExperimentsResponse.data.Experiment.source.authoring.stemmaRid` | string | 是 | — |
| `SearchExperimentsResponse.data.Experiment.source.sdk` | object | 否 | Experiment created from the SDK. |
| `SearchExperimentsResponse.data.Experiment.status` | enum | 是 | The current status of an experiment.<br>示例: `RUNNING` |
| `SearchExperimentsResponse.data.Experiment.statusMessage` | string | 否 | — |
| `SearchExperimentsResponse.data.Experiment.branch` | string | 是 | The name of a Branch.<br>示例: `master` |
| `SearchExperimentsResponse.data.Experiment.parameters` | list<Parameter> | 否 | — |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter` | object | 是 | A parameter with its name and value. |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.name` | string | 是 | The parameter name<br>示例: `learning_rate` |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value` | union | 是 | The parameter value<br>示例: `{"type":"double","value":0.001}` |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.datetime` | object | 否 | A datetime parameter value. |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.datetime.value` | string | 是 | — |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.boolean` | object | 否 | A boolean parameter value. |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.boolean.value` | boolean | 是 | — |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.string` | object | 否 | A string parameter value. |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.string.value` | string | 是 | — |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.double` | object | 否 | A double parameter value. |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.double.value` | number | 是 | — |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.integer` | object | 否 | An integer parameter value. |
| `SearchExperimentsResponse.data.Experiment.parameters.Parameter.value.integer.value` | string | 是 | — |
| `SearchExperimentsResponse.data.Experiment.series` | list<SeriesAggregations> | 否 | — |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations` | object | 是 | Series with precomputed aggregation values. |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations.name` | string | 是 | The series name<br>示例: `loss` |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations.length` | string | 否 | Number of values in the series. This field may be absent when series aggregations are derived from summary metrics rather than the full series data. |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations.value` | union | 是 | Aggregated values for this series<br>示例: `{"type":"double","min":0.05,"max":1.5,"last":0.07}` |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations.value.double` | object | 否 | Aggregated statistics for numeric series. |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations.value.double.min` | number | 否 | Minimum value in the series. Absent if the metric has not been computed. |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations.value.double.max` | number | 否 | Maximum value in the series. Absent if the metric has not been computed. |
| `SearchExperimentsResponse.data.Experiment.series.SeriesAggregations.value.double.last` | number | 否 | Most recent value in the series. Absent if the metric has not been computed. |
| `SearchExperimentsResponse.data.Experiment.summaryMetrics` | list<SummaryMetric> | 否 | — |
| `SearchExperimentsResponse.data.Experiment.summaryMetrics.SummaryMetric` | object | 是 | A summary metric with series name, aggregation type, and computed value. |
| `SearchExperimentsResponse.data.Experiment.summaryMetrics.SummaryMetric.seriesName` | string | 是 | Name of the series this metric belongs to<br>示例: `loss` |
| `SearchExperimentsResponse.data.Experiment.summaryMetrics.SummaryMetric.aggregation` | enum | 是 | Type of aggregation (MIN, MAX, LAST)<br>示例: `MIN` |
| `SearchExperimentsResponse.data.Experiment.summaryMetrics.SummaryMetric.value` | number | 是 | The computed value |
| `SearchExperimentsResponse.data.Experiment.artifacts` | map | 否 | — |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactName` | string | 是 | The name of an experiment artifact. |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactMetadata` | object | 是 | Metadata about an experiment artifact. |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactMetadata.name` | string | 是 | The name of an experiment artifact.<br>示例: `predictions_table` |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactMetadata.description` | string | 否 | — |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactMetadata.sizeBytes` | string | 是 | The size of the file or attachment in bytes.<br>示例: `72526847` |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactMetadata.details` | union | 是 | Details about an experiment artifact.<br>示例: `{"type":"table","rowCount":100}` |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactMetadata.details.table` | object | 否 | Details about a table artifact. |
| `SearchExperimentsResponse.data.Experiment.artifacts.ExperimentArtifactMetadata.details.table.rowCount` | string | 是 | — |
| `SearchExperimentsResponse.data.Experiment.tags` | list<ExperimentTagText> | 否 | — |
| `SearchExperimentsResponse.data.Experiment.tags.ExperimentTagText` | string | 是 | A tag associated with an experiment. |
| `SearchExperimentsResponse.data.Experiment.linkedModelVersion` | string | 否 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `SearchExperimentsResponse.data.Experiment.jobRid` | string | 否 | The RID of a Job.<br>示例: `ri.foundry.main.job.aaf94076-d773-4732-a1df-3b638eb50448` |
| `SearchExperimentsResponse.nextPageToken` | string | 否 | Token for retrieving the next page of results, if more results are available. |

```json
{
  "data": [
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
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `InvalidExperimentSearchFilter` | The search filter is invalid. This can occur when using an unsupported operator and value type<br>combination in a parameter filter, filtering by an unsupported status, or providing a malformed filter. |
| PERMISSION_DENIED | `SearchExperimentsPermissionDenied` | Could not search the Experiment. |
