`GET /api/v2/models/{modelRid}/versions/{modelVersionRid}`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Retrieves a Model Version by its Resource Identifier (RID).

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `modelVersionRid` | string | 是 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ModelVersion**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ModelVersion` | object | 是 | 示例: `{"backingRepositories":["ri.stemma.main.repository.a1b2c3d4-e5f6-7890-abcd-ef1234567890"],"condaRequirements":["numpy==1.24.0","pandas==2.0.0"],"linkedExperiment":"ri.models.main.experiment.abc123","modelApi":{"inputs":[{"name":"input_df","required":true,"type":"tabular","columns":[{"name":"feature_1","required":true,"dataType":{"type":"double"}},{"name":"feature_2","required":true,"dataType":{"type":"integer"}}],"format":"PANDAS"}],"outputs":[{"name":"output_df","required":true,"type":"tabular","columns":[{"name":"prediction","required":true,"dataType":{"type":"double"}}],"format":"SPARK"}]},"createdTime":"2003-05-06T12:34:56.789Z","rid":"ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee"}` |
| `ModelVersion.rid` | string | 是 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `ModelVersion.modelApi` | object | 是 | The Model API is a specification that describes the inputs and outputs of a machine learning model. It is used to define the interface for the model, including the types of data that can be passed to it and the types of data that it will return.<br>示例: `{"inputs":[{"name":"input_df","required":true,"type":"tabular","columns":[{"name":"feature_1","required":true,"dataType":{"type":"double"}},{"name":"feature_2","required":true,"dataType":{"type":"integer"}}],"format":"PANDAS"}],"outputs":[{"name":"output_df","required":true,"type":"tabular","columns":[{"name":"prediction","required":true,"dataType":{"type":"double"}}],"format":"SPARK"}]}` |
| `ModelVersion.modelApi.inputs` | list<ModelApiInput> | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.unsupported` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.unsupported.unsupportedType` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.unsupported.params` | map | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.name` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.date` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.boolean` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.params` | map | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.string` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.array` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.array.itemType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.double` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.integer` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.float` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.any` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.map` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.map.keyType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.map.valueType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.long` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.timestamp` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.name` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns` | list<ModelApiColumn> | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn` | object | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.name` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.required` | boolean | 否 | true by default; false if the column can be null or omitted |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.date` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.boolean` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.params` | map | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.string` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.array` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.array.itemType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.double` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.integer` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.float` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.any` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.map` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.map.keyType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.map.valueType` | union | 是 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.long` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.timestamp` | object | 否 | — |
| `ModelVersion.modelApi.inputs.ModelApiInput.tabular.format` | enum | 否 | Dataframe format the model will receive or is expected to return for this input or output. PANDAS is the default.<br>示例: `PANDAS` |
| `ModelVersion.modelApi.outputs` | list<ModelApiOutput> | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.unsupported` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.unsupportedType` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.params` | map | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.name` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.date` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.boolean` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.params` | map | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.string` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.array` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.array.itemType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.double` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.integer` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.float` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.any` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.map` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.map.keyType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.map.valueType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.long` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.timestamp` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.name` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns` | list<ModelApiColumn> | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn` | object | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.name` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.required` | boolean | 否 | true by default; false if the column can be null or omitted |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.date` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.boolean` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.params` | map | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.string` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.array` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.array.itemType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.double` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.integer` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.float` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.any` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.map` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.map.keyType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.map.valueType` | union | 是 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.long` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.timestamp` | object | 否 | — |
| `ModelVersion.modelApi.outputs.ModelApiOutput.tabular.format` | enum | 否 | Dataframe format the model will receive or is expected to return for this input or output. PANDAS is the default.<br>示例: `PANDAS` |
| `ModelVersion.condaRequirements` | list<string> | 否 | 示例: `["numpy==1.24.0","pandas==2.0.0"]` |
| `ModelVersion.backingRepositories` | list<rid> | 否 | 示例: `["ri.stemma.main.repository.a1b2c3d4-e5f6-7890-abcd-ef1234567890"]` |
| `ModelVersion.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ModelVersion.source` | union | 否 | The source from which this model version was created. |
| `ModelVersion.source.importedContainerizedModel` | object | 否 | Model version imported from a containerized model. |
| `ModelVersion.source.external` | object | 否 | Model version backed by an external model. |
| `ModelVersion.source.codeWorkspace` | object | 否 | Model version created from a code workspace. |
| `ModelVersion.source.codeWorkspace.codeWorkspaceRid` | string | 是 | — |
| `ModelVersion.source.codeWorkspace.branch` | string | 是 | — |
| `ModelVersion.source.modelStudio` | object | 否 | Model version created from Model Studio. |
| `ModelVersion.source.modelStudio.modelStudioRid` | string | 是 | — |
| `ModelVersion.source.codeRepository` | object | 否 | Model version created from a code repository. |
| `ModelVersion.source.codeRepository.repositoryRid` | string | 是 | — |
| `ModelVersion.source.codeRepository.branch` | string | 是 | — |
| `ModelVersion.source.sdk` | object | 否 | Model version created via the SDK. |
| `ModelVersion.source.promoted` | object | 否 | Model version promoted from another model version. |
| `ModelVersion.source.promoted.previousModelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `ModelVersion.source.promoted.previousModelVersionRid` | string | 是 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `ModelVersion.linkedExperiment` | string | 否 | The Experiment linked to this Model Version, if one exists.<br>示例: `ri.models.main.experiment.abc123` |

```json
{
  "backingRepositories": [
    "ri.stemma.main.repository.a1b2c3d4-e5f6-7890-abcd-ef1234567890"
  ],
  "condaRequirements": [
    "numpy==1.24.0",
    "pandas==2.0.0"
  ],
  "linkedExperiment": "ri.models.main.experiment.abc123",
  "modelApi": {
    "inputs": [
      {
        "name": "input_df",
        "required": true,
        "type": "tabular",
        "columns": [
          {
            "name": "feature_1",
            "required": true,
            "dataType": {
              "type": "double"
            }
          },
          {
            "name": "feature_2",
            "required": true,
            "dataType": {
              "type": "integer"
            }
          }
        ],
        "format": "PANDAS"
      }
    ],
    "outputs": [
      {
        "name": "output_df",
        "required": true,
        "type": "tabular",
        "columns": [
          {
            "name": "prediction",
            "required": true,
            "dataType": {
              "type": "double"
            }
          }
        ],
        "format": "SPARK"
      }
    ]
  },
  "createdTime": "2003-05-06T12:34:56.789Z",
  "rid": "ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `UnsupportedModelSource` | The Model Version has a source type that is not supported by the API. This can occur when the model was created through a legacy or internal workflow that is not exposed through the public API. |
| NOT_FOUND | `ModelVersionNotFound` | The given ModelVersion could not be found. |
