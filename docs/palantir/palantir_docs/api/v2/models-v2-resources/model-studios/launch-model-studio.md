`POST /api/v2/models/modelStudios/{modelStudioRid}/launch`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Launches a new training run for the Model Studio using the latest configuration version.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-write`.

**OAuth2 scopes**: `api:models-write`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelStudioRid` | string | 是 | The Resource Identifier (RID) of a Model Studio.<br>示例: `ri.models.main.model-studio.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ModelStudioRun**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ModelStudioRun` | object | 是 | 示例: `{"startedTime":"2003-05-06T12:34:56.789Z","configVersion":1,"startedBy":"f05f8da4-b84c-4fca-9c77-8af0b13d11de","buildRid":"ri.foundry-build.main.build.a1b2c3d4-e5f6-7890-abcd-ef1234567890","resolvedOutputs":{"model":{"type":"model","modelRid":"ri.models.main.model.a1b2c3d4-e5f6-7890-abcd-ef1234567890","modelVersionRid":"ri.models.main.model-version.a1b2c3d4-e5f6-7890-abcd-ef1234567890"}},"runId":"a1b2c3d4e5f6","jobRid":"ri.foundry-build.main.job.a1b2c3d4-e5f6-7890-abcd-ef1234567890","buildStatus":"RUNNING"}` |
| `ModelStudioRun.runId` | string | 是 | A unique identifier for this run, derived from the studio, config, and build.<br>示例: `a1b2c3d4e5f6` |
| `ModelStudioRun.buildRid` | string | 是 | The RID of the build associated with this run.<br>示例: `ri.foundry-build.main.build.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `ModelStudioRun.jobRid` | string | 是 | The RID of the job associated with this run.<br>示例: `ri.foundry-build.main.job.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `ModelStudioRun.configVersion` | integer | 是 | The configuration version used for this run.<br>示例: `1` |
| `ModelStudioRun.startedBy` | string | 是 | The user who started this run.<br>示例: `f05f8da4-b84c-4fca-9c77-8af0b13d11de` |
| `ModelStudioRun.startedTime` | string | 是 | When this run was started.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ModelStudioRun.buildStatus` | enum | 否 | Status of the build.<br>示例: `RUNNING` |
| `ModelStudioRun.resolvedOutputs` | map | 否 | Map of alias to resolved output details (e.g., for models, contains the version RID and experiment). |
| `ModelStudioRun.resolvedOutputs.OutputAlias` | string | 是 | A string alias used to identify outputs in a Model Studio configuration. |
| `ModelStudioRun.resolvedOutputs.ModelStudioRunOutput` | union | 是 | Resolved output details for a Model Studio run. |
| `ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model` | object | 否 | Resolved model output details for a Model Studio run. |
| `ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model.modelRid` | string | 是 | The RID of the model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model.modelVersionRid` | string | 是 | The RID of the model version created by this run.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `ModelStudioRun.resolvedOutputs.ModelStudioRunOutput.model.experimentRid` | string | 否 | The RID of the experiment associated with this run, if any.<br>示例: `ri.models.main.experiment.abc123` |

```json
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
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `ModelStudioNotFound` | The requested Model Studio was not found. |
| PERMISSION_DENIED | `LaunchModelStudioPermissionDenied` | Could not launch the ModelStudio. |
