`POST /api/v2/models/liveDeployments`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Creates a new live deployment for a model version with the specified runtime configuration. The deployment will begin provisioning compute resources and deploying the target model version.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-write`.

**OAuth2 scopes**: `api:models-write`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
{
  "runtimeConfiguration": {
    "minReplicas": 1,
    "maxReplicas": 3,
    "cpu": 1.0,
    "memory": "256MiB",
    "threadCount": 32,
    "environmentVariables": {
      "LOG_LEVEL": "INFO"
    }
  }
}
```

## Response

**LiveDeployment**

The created LiveDeployment

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `LiveDeployment` | object | 是 | The created LiveDeployment<br>示例: `{"runtimeConfiguration":{"minReplicas":1,"maxReplicas":3,"cpu":1.0,"memory":"256MiB","threadCount":32,"environmentVariables":{"LOG_LEVEL":"INFO"}},"modelVersion":{"modelRid":"ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9","modelVersionRid":"ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee"},"rid":"ri.foundry-ml-live.main.live-deployment.f351c142-0e4c-4b12-adc2-6e1539737ae9","branch":"master","status":{"state":"ACTIVE","isReady":true}}` |
| `LiveDeployment.rid` | string | 是 | The Resource Identifier (RID) of a Live Deployment.<br>示例: `ri.foundry-ml-live.main.live-deployment.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `LiveDeployment.modelVersion` | object | 是 | The currently deployed model version.<br>示例: `{"modelRid":"ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9","modelVersionRid":"ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee"}` |
| `LiveDeployment.modelVersion.modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `LiveDeployment.modelVersion.modelVersionRid` | string | 是 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `LiveDeployment.branch` | string | 否 | The model branch this deployment tracks. Present for direct deployments that follow the latest model version on a branch; absent for deployment types that are not branch-scoped.<br>示例: `master` |
| `LiveDeployment.runtimeConfiguration` | object | 是 | The compute resource configuration for the deployment.<br>示例: `{"minReplicas":1,"maxReplicas":3,"cpu":1.0,"memory":"256MiB","threadCount":32,"environmentVariables":{"LOG_LEVEL":"INFO"}}` |
| `LiveDeployment.runtimeConfiguration.minReplicas` | integer | 是 | The minimum number of replicas to keep running. |
| `LiveDeployment.runtimeConfiguration.maxReplicas` | integer | 是 | The maximum number of replicas to scale to under load. |
| `LiveDeployment.runtimeConfiguration.cpu` | number | 否 | The number of CPU units requested. This is also set as the limit. |
| `LiveDeployment.runtimeConfiguration.memory` | string | 否 | The amount of memory requested in human-readable format (e.g. "256MiB", "1GiB"). This is also set as the limit. |
| `LiveDeployment.runtimeConfiguration.gpu` | object | 否 | Optional GPU resources for the deployment.<br>示例: `{"type":"A100"}` |
| `LiveDeployment.runtimeConfiguration.gpu.count` | integer | 是 | The number of GPU units requested (e.g. 1). |
| `LiveDeployment.runtimeConfiguration.gpu.type` | enum | 否 | The specific type of GPU to use. Not setting a type means any type is acceptable.<br>示例: `A100` |
| `LiveDeployment.runtimeConfiguration.threadCount` | integer | 否 | The number of threads used for query handling. Defaults to 32 if not specified. Also affects how many concurrent requests will be sent to a single replica. |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration` | object | 否 | Autoscaling configuration for the deployment. Controls how the deployment scales replicas up and down based on load.<br>示例: `{"scaleUpDelay":{"unit":"SECONDS","value":30},"scaleDownDelay":{"unit":"SECONDS","value":30}}` |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration.scaleUpLoadThreshold` | number | 是 | A threshold between 0.0 and 1.0. If the ratio of running jobs to job capacity exceeds this threshold for the duration of the scale-up delay, the deployment will scale up. Job capacity is the number of running replicas multiplied by the thread count (concurrency limit). |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration.scaleUpDelay` | object | 是 | The duration that load must exceed the scale-up threshold before scaling up.<br>示例: `{"unit":"SECONDS","value":30}` |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration.scaleUpDelay.value` | integer | 是 | The duration value.<br>示例: `30` |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration.scaleUpDelay.unit` | enum | 是 | The unit of duration.<br>示例: `SECONDS` |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration.scaleDownDelay` | object | 是 | The duration that load must be below the scale-down threshold before scaling down.<br>示例: `{"unit":"SECONDS","value":30}` |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration.scaleDownDelay.value` | integer | 是 | The duration value.<br>示例: `30` |
| `LiveDeployment.runtimeConfiguration.scalingConfiguration.scaleDownDelay.unit` | enum | 是 | The unit of duration.<br>示例: `SECONDS` |
| `LiveDeployment.runtimeConfiguration.sources` | list<ConnectionRid> | 否 | The Connection (also known as source) RIDs attached to the deployment. |
| `LiveDeployment.runtimeConfiguration.sources.ConnectionRid` | string | 是 | The Resource Identifier (RID) of a Connection (also known as a source). |
| `LiveDeployment.runtimeConfiguration.environmentVariables` | map | 否 | User-supplied environment variables to set on the deployment container, keyed by variable name. |
| `LiveDeployment.status` | object | 是 | The current operational status of the deployment.<br>示例: `{"state":"ACTIVE","isReady":true}` |
| `LiveDeployment.status.state` | enum | 是 | The current operational state of the deployment.<br>示例: `ACTIVE` |
| `LiveDeployment.status.isReady` | boolean | 是 | Whether the deployment is ready to serve inference requests. A deployment may be active but not ready if it has been autoscaled to zero replicas. |

```json
{
  "runtimeConfiguration": {
    "minReplicas": 1,
    "maxReplicas": 3,
    "cpu": 1.0,
    "memory": "256MiB",
    "threadCount": 32,
    "environmentVariables": {
      "LOG_LEVEL": "INFO"
    }
  },
  "modelVersion": {
    "modelRid": "ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9",
    "modelVersionRid": "ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee"
  },
  "rid": "ri.foundry-ml-live.main.live-deployment.f351c142-0e4c-4b12-adc2-6e1539737ae9",
  "branch": "master",
  "status": {
    "state": "ACTIVE",
    "isReady": true
  }
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `ThreadCountTooHigh` | The specified thread count exceeds the maximum allowed value. |
| INVALID_ARGUMENT | `InvalidGpuCount` | The GPU count is invalid. The GPU count must be between 1 and the maximum allowed<br>for the requested GPU type. |
| INVALID_ARGUMENT | `GpuTypeNotAvailable` | The requested GPU type is not available. Use a GPU type that is available in<br>the deployment's resource queue. |
| INVALID_ARGUMENT | `ReservedEnvironmentVariable` | The provided environment variable name is reserved for use by the deployment runtime. |
| INVALID_ARGUMENT | `DeploymentSourceNotImportedIntoProject` | The source must be in, or imported into, the live deployment's project. |
| INVALID_ARGUMENT | `DeploymentSourceCannotExportResourceMarkings` | The source does not have exports enabled for all effective markings on the deployed resource. |
| INVALID_ARGUMENT | `DeploymentSourceNotEnabledForComputeModules` | The source is not enabled for use by Compute Modules. |
| PERMISSION_DENIED | `CreateLiveDeploymentPermissionDenied` | Could not create the LiveDeployment. |
| NOT_FOUND | `ModelNotFound` | The given Model could not be found. |
