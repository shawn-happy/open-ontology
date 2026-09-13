`POST /api/v2/functions/queries/getByRidBatch`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets a list of query types by RID in bulk. By default, this gets the latest version of each query.

Queries are filtered from the response if they don't exist or the requesting token lacks the required 
permissions.

The maximum batch size for this endpoint is 100.

Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:functions-read`.

**OAuth2 scopes**: `api:functions-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Request body

```json
[
  {
    "rid": "ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c",
    "version": "1.2.3"
  }
]
```

## Response

**GetByRidQueriesBatchResponse**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetByRidQueriesBatchResponse` | object | 是 | 示例: `{"data":[{"output":{"type":"integer"},"apiName":"myQueryFunction","displayName":"My Entity","rid":"ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c","typeReferences":{"MyTypeReference":{"type":"integer"}},"parameters":{"price":{"dataType":{"type":"integer"}}},"version":"1.2.3"}]}` |
| `GetByRidQueriesBatchResponse.data` | list<Query> | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query` | object | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.apiName` | string | 是 | The name of the Query in the API.<br>示例: `myQueryFunction` |
| `GetByRidQueriesBatchResponse.data.Query.description` | string | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.displayName` | string | 否 | The display name of the entity. |
| `GetByRidQueriesBatchResponse.data.Query.parameters` | map | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.ParameterId` | string | 是 | The unique identifier of the parameter. Parameters are used as inputs when an action or query is applied.<br>Parameters can be viewed and managed in the **Ontology Manager**. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter` | object | 是 | Details about a parameter of a query. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.description` | string | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.struct` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.struct.fields` | list<QueryStructField> | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.struct.fields.QueryStructField` | object | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`.<br>示例: `productId` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.set` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.set.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.void` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType` | object | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.union` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.union.unionTypes` | list<QueryDataType> | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.float` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.long` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.unsupported` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.unsupported.unsupportedType` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.unsupported.params` | map | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.attachment` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.mediaReference` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.null` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.array` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.array.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.valueTypeReference` | object | 否 | A reference to a value type that has been registered in the Ontology. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.valueTypeReference.rid` | string | 是 | The RID of a value type that has been registered in the Ontology.<br>示例: `ri.type-registry.main.value-type.f5ee06ef-6dfd-4d91-a01e-91bd457c719d` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.valueTypeReference.versionId` | string | 是 | The version ID of a value type that has been registered in the Ontology.<br>示例: `00000000-0000-0000-0000-000000000001` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query.<br>示例: `MyTypeReference` |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.dataType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.parameters.Parameter.required` | boolean | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.output` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.output.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.struct` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.struct.fields` | list<QueryStructField> | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.struct.fields.QueryStructField` | object | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`.<br>示例: `productId` |
| `GetByRidQueriesBatchResponse.data.Query.output.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.output.set` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.set.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.output.void` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType` | object | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.union` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.union.unionTypes` | list<QueryDataType> | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `GetByRidQueriesBatchResponse.data.Query.output.float` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.long` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.unsupported` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.unsupported.unsupportedType` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.unsupported.params` | map | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.attachment` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.mediaReference` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.null` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.array` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.array.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.output.valueTypeReference` | object | 否 | A reference to a value type that has been registered in the Ontology. |
| `GetByRidQueriesBatchResponse.data.Query.output.valueTypeReference.rid` | string | 是 | The RID of a value type that has been registered in the Ontology.<br>示例: `ri.type-registry.main.value-type.f5ee06ef-6dfd-4d91-a01e-91bd457c719d` |
| `GetByRidQueriesBatchResponse.data.Query.output.valueTypeReference.versionId` | string | 是 | The version ID of a value type that has been registered in the Ontology.<br>示例: `00000000-0000-0000-0000-000000000001` |
| `GetByRidQueriesBatchResponse.data.Query.output.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `GetByRidQueriesBatchResponse.data.Query.output.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query.<br>示例: `MyTypeReference` |
| `GetByRidQueriesBatchResponse.data.Query.output.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.rid` | string | 是 | The unique resource identifier of a Function, useful for interacting with other Foundry APIs.<br>示例: `ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c` |
| `GetByRidQueriesBatchResponse.data.Query.version` | string | 是 | The version of the given Function, written `<major>.<minor>.<patch>-<tag>`, where `-<tag>` is optional.<br>Examples: `1.2.3`, `1.2.3-rc1`.<br>示例: `1.2.3` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences` | map | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.TypeReferenceIdentifier` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.struct` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.struct.fields` | list<QueryStructField> | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.struct.fields.QueryStructField` | object | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`.<br>示例: `productId` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.set` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.set.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.void` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType` | object | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.union` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.union.unionTypes` | list<QueryDataType> | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.float` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.long` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.unsupported` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.unsupported.unsupportedType` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.unsupported.params` | map | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.attachment` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.mediaReference` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.null` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.array` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.array.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.valueTypeReference` | object | 否 | A reference to a value type that has been registered in the Ontology. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.valueTypeReference.rid` | string | 是 | The RID of a value type that has been registered in the Ontology.<br>示例: `ri.type-registry.main.value-type.f5ee06ef-6dfd-4d91-a01e-91bd457c719d` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.valueTypeReference.versionId` | string | 是 | The version ID of a value type that has been registered in the Ontology.<br>示例: `00000000-0000-0000-0000-000000000001` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query.<br>示例: `MyTypeReference` |
| `GetByRidQueriesBatchResponse.data.Query.typeReferences.QueryDataType.timestamp` | object | 否 | — |

```json
{
  "data": [
    {
      "output": {
        "type": "integer"
      },
      "apiName": "myQueryFunction",
      "displayName": "My Entity",
      "rid": "ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c",
      "typeReferences": {
        "MyTypeReference": {
          "type": "integer"
        }
      },
      "parameters": {
        "price": {
          "dataType": {
            "type": "integer"
          }
        }
      },
      "version": "1.2.3"
    }
  ]
}
```
