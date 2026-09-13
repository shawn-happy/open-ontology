`POST /api/v2/models/liveDeployments/{liveDeploymentRid}/transformJson`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Performs inference on the live deployment.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-execute`.

**OAuth2 scopes**: `api:models-execute`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `liveDeploymentRid` | string | 是 | The Resource Identifier (RID) of a Live Deployment.<br>示例: `ri.foundry-ml-live.main.live-deployment.f351c142-0e4c-4b12-adc2-6e1539737ae9` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "input": {
    "input_df": [
      {
        "feature_1": 1.0,
        "feature_2": 2
      }
    ]
  }
}
```

## Response

**TransformLiveDeploymentResponse**

The response from transforming input data using a live deployment.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `TransformLiveDeploymentResponse` | object | 是 | The response from transforming input data using a live deployment. |
| `TransformLiveDeploymentResponse.output` | map | 否 | The output data from the model inference. The structure depends on the model's defined API specification, where each key is an output name and the value is the corresponding output data. |

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| NOT_FOUND | `LiveDeploymentNotFound` | The specified live deployment was not found. |
| TIMEOUT | `InferenceTimeout` | The live deployment took longer than 5 minutes to respond to the inference request.<br>This typically indicates the model execution is taking too long or the deployment is under heavy load. |
| INVALID_ARGUMENT | `InferenceInvalidInput` | The inference request contains invalid input data that does not match the model's API specification.<br>Check the error type for specific validation failure details. |
| INVALID_ARGUMENT | `InferenceFailure` | The inference request failed due to a model execution error or unexpected internal issue.<br>This typically indicates a problem with the model itself rather than the input data. |
| PERMISSION_DENIED | `TransformJsonLiveDeploymentPermissionDenied` | Could not transformJson the LiveDeployment. |
