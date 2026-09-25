`GET /api/v2/models/modelStudios/{modelStudioRid}/runs`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Lists all runs for a Model Studio.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelStudioRid` | string | 是 | The Resource Identifier (RID) of a Model Studio.<br>示例: `ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `configVersion` | integer | 否 | Filter runs by configuration version.<br>示例: `1` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListModelStudioRunsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListModelStudioRunsResponse` | object | 是 | 示例: `{"data":[{"startedTime":"2003-05-06T12:34:56.789Z","configVersion":1,"startedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","buildRid":"ri.foundry-build.main.build.a1b2c3d4-e5f6-7890-abcd-ef1234567890","resolvedOutputs":{"model":{"type":"model","modelRid":"ri.models.main.model.a1b2c3d4-e5f6-7890-abcd-ef1234567890","modelVersionRid":"ri.models.main.model-version.a1b2c3d4-e5f6-7890-abcd-ef1234567890"}},"runId":"a1b2c3d4e5f6","jobRid":"ri.foundry-build.main.job.a1b2c3d4-e5f6-7890-abcd-ef1234567890","buildStatus":"RUNNING"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListModelStudioRunsResponse.data` | list<ModelStudioRun> | 否 | — |
| `ListModelStudioRunsResponse.data.ModelStudioRun` | object | 是 | — |
| `ListModelStudioRunsResponse.data.ModelStudioRun.runId` | string | 是 | A unique identifier for this run, derived from the studio, config, and build.<br>示例: `a1b2c3d4e5f6` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.buildRid` | string | 是 | The RID of the build associated with this run.<br>示例: `ri.foundry-build.main.build.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.jobRid` | string | 是 | The RID of the job associated with this run.<br>示例: `ri.foundry-build.main.job.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.configVersion` | integer | 是 | The configuration version used for this run.<br>示例: `1` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.startedBy` | string | 是 | The user who started this run.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.startedTime` | string | 是 | When this run was started.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.buildStatus` | enum | 否 | Status of the build.<br>示例: `RUNNING` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.resolvedOutputs` | map | 否 | Map of alias to resolved output details (e.g., for models, contains the version RID and experiment). |
| `ListModelStudioRunsResponse.data.ModelStudioRun.resolvedOutputs.OutputAlias` | string | 是 | A string alias used to identify outputs in a Model Studio configuration. |
| `ListModelStudioRunsResponse.data.ModelStudioRun.resolvedOutputs.ModelStudioRunOutput` | union | 是 | Resolved output details for a Model Studio run. |
| `ListModelStudioRunsResponse.data.ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model` | object | 否 | Resolved model output details for a Model Studio run. |
| `ListModelStudioRunsResponse.data.ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model.modelRid` | string | 是 | The RID of the model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model.modelVersionRid` | string | 是 | The RID of the model version created by this run.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `ListModelStudioRunsResponse.data.ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model.experimentRid` | string | 否 | The RID of the experiment associated with this run, if any.<br>示例: `ri.models.main.experiment.abc123` |
| `ListModelStudioRunsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "startedTime": "2003-05-06T12:34:56.789Z",
      "configVersion": 1,
      "startedBy": "f05f8da4-b84c-4fca-9c77-8af0b13d11de",
      "buildRid": "ri.foundry-build.main.build.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "resolvedOutputs": {
        "model": {
          "type": "model",
          "modelRid": "ri.models.main.model.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
          "modelVersionRid": "ri.models.main.model-version.a1b2c3d4-e5f6-7890-abcd-ef1234567890"
        }
      },
      "runId": "a1b2c3d4e5f6",
      "jobRid": "ri.foundry-build.main.job.a1b2c3d4-e5f6-7890-abcd-ef1234567890",
      "buildStatus": "RUNNING"
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ModelStudioNotFound` | The requested Model Studio was not found. |
| NOT_FOUND | `ModelStudioConfigVersionNotFound` | The requested Model Studio configuration version was not found. |
