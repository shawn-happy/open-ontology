`GET /api/v2/functions/queries/getByRid`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets a specific query type with the given RID. By default, this gets the latest version of the query.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:functions-read`.

**OAuth2 scopes**: `api:functions-read`

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `rid` | string | 是 | The unique resource identifier of a Function, useful for interacting with other Foundry APIs.<br>示例: `ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c` |
| `version` | string | 否 | The version of the given Function, written `<major>.<minor>.<patch>-<tag>`, where `-<tag>` is optional.<br>Examples: `1.2.3`, `1.2.3-rc1`.<br>示例: `1.2.3` |
| `includePrerelease` | boolean | 否 | When no version is specified and this flag is set to true, the latest version resolution will consider<br>prerelease versions (e.g., 1.2.3-beta could be returned as the latest). When false, only stable<br>versions are considered when determining the latest version.<br>Defaults to false. |
| `preview` | boolean | 否 | Enables the use of preview functionality.<br>示例: `true` |

## Response

**Query**

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `Query` | object | 是 | 示例: `{"output":{"type":"integer"},"apiName":"myQueryFunction","displayName":"My Entity","rid":"ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c","typeReferences":{"MyTypeReference":{"type":"integer"}},"parameters":{"price":{"dataType":{"type":"integer"}}},"version":"1.2.3"}` |
| `Query.apiName` | string | 是 | The name of the Query in the API.<br>示例: `myQueryFunction` |
| `Query.description` | string | 否 | — |
| `Query.displayName` | string | 否 | The display name of the entity. |
| `Query.parameters` | map | 否 | — |
| `Query.parameters.ParameterId` | string | 是 | The unique identifier of the parameter. Parameters are used as inputs when an action or query is applied.<br>Parameters can be viewed and managed in the **Ontology Manager**. |
| `Query.parameters.Parameter` | object | 是 | Details about a parameter of a query. |
| `Query.parameters.Parameter.description` | string | 否 | — |
| `Query.parameters.Parameter.dataType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.parameters.Parameter.dataType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.struct` | object | 否 | — |
| `Query.parameters.Parameter.dataType.struct.fields` | list<QueryStructField> | 否 | — |
| `Query.parameters.Parameter.dataType.struct.fields.QueryStructField` | object | 是 | — |
| `Query.parameters.Parameter.dataType.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`.<br>示例: `productId` |
| `Query.parameters.Parameter.dataType.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.parameters.Parameter.dataType.set` | object | 否 | — |
| `Query.parameters.Parameter.dataType.set.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.parameters.Parameter.dataType.void` | object | 否 | — |
| `Query.parameters.Parameter.dataType.string` | object | 否 | — |
| `Query.parameters.Parameter.dataType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.integer` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType` | object | 是 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.union` | object | 否 | — |
| `Query.parameters.Parameter.dataType.union.unionTypes` | list<QueryDataType> | 否 | — |
| `Query.parameters.Parameter.dataType.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `Query.parameters.Parameter.dataType.float` | object | 否 | — |
| `Query.parameters.Parameter.dataType.long` | object | 否 | — |
| `Query.parameters.Parameter.dataType.boolean` | object | 否 | — |
| `Query.parameters.Parameter.dataType.unsupported` | object | 否 | — |
| `Query.parameters.Parameter.dataType.unsupported.unsupportedType` | string | 是 | — |
| `Query.parameters.Parameter.dataType.unsupported.params` | map | 否 | — |
| `Query.parameters.Parameter.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `Query.parameters.Parameter.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `Query.parameters.Parameter.dataType.attachment` | object | 否 | — |
| `Query.parameters.Parameter.dataType.mediaReference` | object | 否 | — |
| `Query.parameters.Parameter.dataType.null` | object | 否 | — |
| `Query.parameters.Parameter.dataType.array` | object | 否 | — |
| `Query.parameters.Parameter.dataType.array.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `Query.parameters.Parameter.dataType.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.dataType.valueTypeReference` | object | 否 | A reference to a value type that has been registered in the Ontology. |
| `Query.parameters.Parameter.dataType.valueTypeReference.rid` | string | 是 | The RID of a value type that has been registered in the Ontology.<br>示例: `ri.type-registry.main.value-type.f5ee06ef-6dfd-4d91-a01e-91bd457c719d` |
| `Query.parameters.Parameter.dataType.valueTypeReference.versionId` | string | 是 | The version ID of a value type that has been registered in the Ontology.<br>示例: `00000000-0000-0000-0000-000000000001` |
| `Query.parameters.Parameter.dataType.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `Query.parameters.Parameter.dataType.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query.<br>示例: `MyTypeReference` |
| `Query.parameters.Parameter.dataType.timestamp` | object | 否 | — |
| `Query.parameters.Parameter.required` | boolean | 是 | — |
| `Query.output` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.output.date` | object | 否 | — |
| `Query.output.struct` | object | 否 | — |
| `Query.output.struct.fields` | list<QueryStructField> | 否 | — |
| `Query.output.struct.fields.QueryStructField` | object | 是 | — |
| `Query.output.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`.<br>示例: `productId` |
| `Query.output.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.output.set` | object | 否 | — |
| `Query.output.set.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.output.void` | object | 否 | — |
| `Query.output.string` | object | 否 | — |
| `Query.output.double` | object | 否 | — |
| `Query.output.integer` | object | 否 | — |
| `Query.output.threeDimensionalAggregation` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.output.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.output.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType` | object | 是 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.output.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.output.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `Query.output.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `Query.output.union` | object | 否 | — |
| `Query.output.union.unionTypes` | list<QueryDataType> | 否 | — |
| `Query.output.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `Query.output.float` | object | 否 | — |
| `Query.output.long` | object | 否 | — |
| `Query.output.boolean` | object | 否 | — |
| `Query.output.unsupported` | object | 否 | — |
| `Query.output.unsupported.unsupportedType` | string | 是 | — |
| `Query.output.unsupported.params` | map | 否 | — |
| `Query.output.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `Query.output.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `Query.output.attachment` | object | 否 | — |
| `Query.output.mediaReference` | object | 否 | — |
| `Query.output.null` | object | 否 | — |
| `Query.output.array` | object | 否 | — |
| `Query.output.array.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.output.twoDimensionalAggregation` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.output.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.output.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.output.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `Query.output.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `Query.output.valueTypeReference` | object | 否 | A reference to a value type that has been registered in the Ontology. |
| `Query.output.valueTypeReference.rid` | string | 是 | The RID of a value type that has been registered in the Ontology.<br>示例: `ri.type-registry.main.value-type.f5ee06ef-6dfd-4d91-a01e-91bd457c719d` |
| `Query.output.valueTypeReference.versionId` | string | 是 | The version ID of a value type that has been registered in the Ontology.<br>示例: `00000000-0000-0000-0000-000000000001` |
| `Query.output.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `Query.output.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query.<br>示例: `MyTypeReference` |
| `Query.output.timestamp` | object | 否 | — |
| `Query.rid` | string | 是 | The unique resource identifier of a Function, useful for interacting with other Foundry APIs.<br>示例: `ri.function-registry.main.function.8cb2d957-f0e6-4e0c-81de-e701bd54b18c` |
| `Query.version` | string | 是 | The version of the given Function, written `<major>.<minor>.<patch>-<tag>`, where `-<tag>` is optional.<br>Examples: `1.2.3`, `1.2.3-rc1`.<br>示例: `1.2.3` |
| `Query.typeReferences` | map | 否 | — |
| `Query.typeReferences.TypeReferenceIdentifier` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query. |
| `Query.typeReferences.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `Query.typeReferences.QueryDataType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.struct` | object | 否 | — |
| `Query.typeReferences.QueryDataType.struct.fields` | list<QueryStructField> | 否 | — |
| `Query.typeReferences.QueryDataType.struct.fields.QueryStructField` | object | 是 | — |
| `Query.typeReferences.QueryDataType.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`.<br>示例: `productId` |
| `Query.typeReferences.QueryDataType.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.typeReferences.QueryDataType.set` | object | 否 | — |
| `Query.typeReferences.QueryDataType.set.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.typeReferences.QueryDataType.void` | object | 否 | — |
| `Query.typeReferences.QueryDataType.string` | object | 否 | — |
| `Query.typeReferences.QueryDataType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.integer` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType` | object | 是 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.union` | object | 否 | — |
| `Query.typeReferences.QueryDataType.union.unionTypes` | list<QueryDataType> | 否 | — |
| `Query.typeReferences.QueryDataType.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Query parameters or outputs. |
| `Query.typeReferences.QueryDataType.float` | object | 否 | — |
| `Query.typeReferences.QueryDataType.long` | object | 否 | — |
| `Query.typeReferences.QueryDataType.boolean` | object | 否 | — |
| `Query.typeReferences.QueryDataType.unsupported` | object | 否 | — |
| `Query.typeReferences.QueryDataType.unsupported.unsupportedType` | string | 是 | — |
| `Query.typeReferences.QueryDataType.unsupported.params` | map | 否 | — |
| `Query.typeReferences.QueryDataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `Query.typeReferences.QueryDataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `Query.typeReferences.QueryDataType.attachment` | object | 否 | — |
| `Query.typeReferences.QueryDataType.mediaReference` | object | 否 | — |
| `Query.typeReferences.QueryDataType.null` | object | 否 | — |
| `Query.typeReferences.QueryDataType.array` | object | 否 | — |
| `Query.typeReferences.QueryDataType.array.subType` | union | 是 | A union of all the types supported by Query parameters or outputs.<br>示例: `{"type":"integer"}` |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `Query.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `Query.typeReferences.QueryDataType.valueTypeReference` | object | 否 | A reference to a value type that has been registered in the Ontology. |
| `Query.typeReferences.QueryDataType.valueTypeReference.rid` | string | 是 | The RID of a value type that has been registered in the Ontology.<br>示例: `ri.type-registry.main.value-type.f5ee06ef-6dfd-4d91-a01e-91bd457c719d` |
| `Query.typeReferences.QueryDataType.valueTypeReference.versionId` | string | 是 | The version ID of a value type that has been registered in the Ontology.<br>示例: `00000000-0000-0000-0000-000000000001` |
| `Query.typeReferences.QueryDataType.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `Query.typeReferences.QueryDataType.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query.<br>示例: `MyTypeReference` |
| `Query.typeReferences.QueryDataType.timestamp` | object | 否 | — |

```json
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
```

## Error responses

| 错误码 | 错误名称 | 说明 |
| --- | --- | --- |
| PERMISSION_DENIED | `GetByRidPermissionDenied` | Could not getByRid the Query. |
