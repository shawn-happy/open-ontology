`GET /api/v2/models/{modelRid}/experiments/{experimentRid}/artifactTables/{experimentArtifactTableName}/json`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Read table data from an experiment artifact as a streamed binary response containing JSON.
The response body is a JSON array of row objects, where each object maps column names to values.
Results are paginated by row count with a default page size of 10 and a maximum of 100.


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
| `pageSize` | integer | 否 | Maximum number of rows to return. Default is 10, maximum is 100. |
| `offset` | integer | 否 | Number of rows to skip from the beginning. Defaults to 0. |
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
| PERMISSION_DENIED | `JsonExperimentArtifactTablePermissionDenied` | Could not json the ExperimentArtifactTable. |
