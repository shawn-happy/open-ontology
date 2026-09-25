`GET /api/v2/models/{modelRid}/versions`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Lists all Model Versions for a given Model.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:models-read`.

**OAuth2 scopes**: `api:models-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `modelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branch` | string | 否 | The branch to list versions from. Defaults to master on most enrollments.<br>示例: `master` |
| `pageSize` | integer | 否 | The page size to use for the endpoint. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**ListModelVersionsResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListModelVersionsResponse` | object | 是 | 示例: `{"data":[{"backingRepositories":["ri.stemma.main.repository.a1b2c3d4-e5f6-7890-abcd-ef1234567890"],"condaRequirements":["numpy==1.24.0","pandas==2.0.0"],"linkedExperiment":"ri.models.main.experiment.abc123","modelApi":{"inputs":[{"name":"input_df","required":true,"type":"tabular","columns":[{"name":"feature_1","required":true,"dataType":{"type":"double"}},{"name":"feature_2","required":true,"dataType":{"type":"integer"}}],"format":"PANDAS"}],"outputs":[{"name":"output_df","required":true,"type":"tabular","columns":[{"name":"prediction","required":true,"dataType":{"type":"double"}}],"format":"SPARK"}]},"createdTime":"2003-05-06T12:34:56.789Z","rid":"ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee"}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"}` |
| `ListModelVersionsResponse.data` | list<ModelVersion> | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion` | object | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.rid` | string | 是 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `ListModelVersionsResponse.data.ModelVersion.modelApi` | object | 是 | The Model API is a specification that describes the inputs and outputs of a machine learning model. It is used to define the interface for the model, including the types of data that can be passed to it and the types of data that it will return.<br>示例: `{"inputs":[{"name":"input_df","required":true,"type":"tabular","columns":[{"name":"feature_1","required":true,"dataType":{"type":"double"}},{"name":"feature_2","required":true,"dataType":{"type":"integer"}}],"format":"PANDAS"}],"outputs":[{"name":"output_df","required":true,"type":"tabular","columns":[{"name":"prediction","required":true,"dataType":{"type":"double"}}],"format":"SPARK"}]}` |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs` | list<ModelApiInput> | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.unsupported` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.unsupported.unsupportedType` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.unsupported.params` | map | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.name` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.date` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.boolean` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.params` | map | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.string` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.array` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.array.itemType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.double` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.integer` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.float` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.any` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.map` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.map.keyType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.map.valueType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.long` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.parameter.dataType.timestamp` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.name` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns` | list<ModelApiColumn> | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn` | object | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.name` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.required` | boolean | 否 | true by default; false if the column can be null or omitted |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.date` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.boolean` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.params` | map | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.string` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.array` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.array.itemType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.double` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.integer` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.float` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.any` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.map` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.map.keyType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.map.valueType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.long` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.columns.ModelApiColumn.dataType.timestamp` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.inputs.ModelApiInput.tabular.format` | enum | 否 | Dataframe format the model will receive or is expected to return for this input or output. PANDAS is the default.<br>示例: `PANDAS` |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs` | list<ModelApiOutput> | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.unsupported` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.unsupportedType` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.params` | map | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.name` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.date` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.boolean` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.params` | map | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.string` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.array` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.array.itemType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.double` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.integer` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.float` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.any` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.map` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.map.keyType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.map.valueType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.long` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.parameter.dataType.timestamp` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.name` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.required` | boolean | 否 | true by default; false if the input or output can be null or omitted |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns` | list<ModelApiColumn> | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn` | object | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.name` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.required` | boolean | 否 | true by default; false if the column can be null or omitted |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.date` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.boolean` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.unsupportedType` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.params` | map | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.string` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.array` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.array.itemType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.double` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.integer` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.float` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.any` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.map` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.map.keyType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.map.valueType` | union | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.long` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.columns.ModelApiColumn.dataType.timestamp` | object | 否 | — |
| `ListModelVersionsResponse.data.ModelVersion.modelApi.outputs.ModelApiOutput.tabular.format` | enum | 否 | Dataframe format the model will receive or is expected to return for this input or output. PANDAS is the default.<br>示例: `PANDAS` |
| `ListModelVersionsResponse.data.ModelVersion.condaRequirements` | list<string> | 否 | 示例: `["numpy==1.24.0","pandas==2.0.0"]` |
| `ListModelVersionsResponse.data.ModelVersion.backingRepositories` | list<rid> | 否 | 示例: `["ri.stemma.main.repository.a1b2c3d4-e5f6-7890-abcd-ef1234567890"]` |
| `ListModelVersionsResponse.data.ModelVersion.createdTime` | string | 是 | The time at which the resource was created.<br>示例: `2003-05-06T12:34:56.789Z` |
| `ListModelVersionsResponse.data.ModelVersion.source` | union | 否 | The source from which this model version was created. |
| `ListModelVersionsResponse.data.ModelVersion.source.importedContainerizedModel` | object | 否 | Model version imported from a containerized model. |
| `ListModelVersionsResponse.data.ModelVersion.source.external` | object | 否 | Model version backed by an external model. |
| `ListModelVersionsResponse.data.ModelVersion.source.codeWorkspace` | object | 否 | Model version created from a code workspace. |
| `ListModelVersionsResponse.data.ModelVersion.source.codeWorkspace.codeWorkspaceRid` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.source.codeWorkspace.branch` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.source.modelStudio` | object | 否 | Model version created from Model Studio. |
| `ListModelVersionsResponse.data.ModelVersion.source.modelStudio.modelStudioRid` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.source.codeRepository` | object | 否 | Model version created from a code repository. |
| `ListModelVersionsResponse.data.ModelVersion.source.codeRepository.repositoryRid` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.source.codeRepository.branch` | string | 是 | — |
| `ListModelVersionsResponse.data.ModelVersion.source.sdk` | object | 否 | Model version created via the SDK. |
| `ListModelVersionsResponse.data.ModelVersion.source.promoted` | object | 否 | Model version promoted from another model version. |
| `ListModelVersionsResponse.data.ModelVersion.source.promoted.previousModelRid` | string | 是 | The Resource Identifier (RID) of a Model.<br>示例: `ri.models.main.model.f351c142-0e4c-4b12-adc2-6e1539737ae9` |
| `ListModelVersionsResponse.data.ModelVersion.source.promoted.previousModelVersionRid` | string | 是 | The Resource Identifier (RID) of a Model Version.<br>示例: `ri.models.main.model-version.adf94926-c3ac-41ea-beb2-4946699d08ee` |
| `ListModelVersionsResponse.data.ModelVersion.linkedExperiment` | string | 否 | The Experiment linked to this Model Version, if one exists.<br>示例: `ri.models.main.experiment.abc123` |
| `ListModelVersionsResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
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
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| INVALID_ARGUMENT | `UnsupportedModelSource` | The Model Version has a source type that is not supported by the API. This can occur when the model was created through a legacy or internal workflow that is not exposed through the public API. |
| NOT_FOUND | `ModelNotFound` | The given Model could not be found. |
