`GET /api/v2/ontologies/{ontology}/queryTypes/{queryApiName}`

Gets a specific query type with the given API name.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `queryApiName` | string | 是 | The API name of the query type. To find the API name, use the **List query types** endpoint or<br>check the **Ontology Manager**.<br>示例: `getEmployeesInCity` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `version` | string | 否 | The version of the Query to get. If not specified, the latest version is used. The latest version is<br>the one that was most recently published, including pre-release versions. |
| `branch` | string | 否 | The Foundry branch to load Query metadata from. Branches are an experimental feature and not all workflows<br>are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |

## Response

**QueryTypeV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `QueryTypeV2` | object | 是 | Success response.<br>示例: `{"apiName":"getEmployeesInCity","displayName":"Get Employees in City","description":"Gets all employees in a given city","parameters":{"city":{"dataType":{"type":"string"},"description":"The city to search for employees in","required":true}},"output":{"dataType":{"type":"array","subType":{"type":"object","objectApiName":"Employee"}},"required":true},"rid":"ri.function-registry.main.function.f05481407-1d67-4120-83b4-e3fed5305a29b","version":"1.1.3-rc1"}` |
| `QueryTypeV2.apiName` | string | 是 | The name of the Query in the API. |
| `QueryTypeV2.description` | string | 否 | — |
| `QueryTypeV2.displayName` | string | 否 | The display name of the entity. |
| `QueryTypeV2.parameters` | map | 否 | — |
| `QueryTypeV2.parameters.ParameterId` | string | 是 | The unique identifier of the parameter. Parameters are used as inputs when an action or query is applied.<br>Parameters can be viewed and managed in the **Ontology Manager**. |
| `QueryTypeV2.parameters.QueryParameterV2` | object | 是 | Details about a parameter of a query. |
| `QueryTypeV2.parameters.QueryParameterV2.description` | string | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.interfaceObject` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.interfaceObject.interfaceTypeApiName` | string | 否 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.struct` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.struct.fields` | list<QueryStructField> | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.struct.fields.QueryStructField` | object | 是 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.string` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.integer` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType` | object | 是 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.float` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.long` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.unsupported` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.unsupported.unsupportedType` | string | 是 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.unsupported.params` | map | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.attachment` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.array` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.array.subType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.objectSet` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.objectSet.objectApiName` | string | 否 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.objectSet.objectTypeApiName` | string | 否 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.timestamp` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.set` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.set.subType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.void` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.entrySet` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.entrySet.keyType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.entrySet.valueType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.double` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.union` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.union.unionTypes` | list<QueryDataType> | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.boolean` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.mediaReference` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.null` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.interfaceObjectSet` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.interfaceObjectSet.interfaceTypeApiName` | string | 是 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.object` | object | 否 | — |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.object.objectApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.parameters.QueryParameterV2.dataType.object.objectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.parameters.QueryParameterV2.required` | boolean | 是 | — |
| `QueryTypeV2.output` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.output.date` | object | 否 | — |
| `QueryTypeV2.output.interfaceObject` | object | 否 | — |
| `QueryTypeV2.output.interfaceObject.interfaceTypeApiName` | string | 否 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.output.struct` | object | 否 | — |
| `QueryTypeV2.output.struct.fields` | list<QueryStructField> | 否 | — |
| `QueryTypeV2.output.struct.fields.QueryStructField` | object | 是 | — |
| `QueryTypeV2.output.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`. |
| `QueryTypeV2.output.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.output.string` | object | 否 | — |
| `QueryTypeV2.output.integer` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType` | object | 是 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `QueryTypeV2.output.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.float` | object | 否 | — |
| `QueryTypeV2.output.long` | object | 否 | — |
| `QueryTypeV2.output.unsupported` | object | 否 | — |
| `QueryTypeV2.output.unsupported.unsupportedType` | string | 是 | — |
| `QueryTypeV2.output.unsupported.params` | map | 否 | — |
| `QueryTypeV2.output.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `QueryTypeV2.output.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `QueryTypeV2.output.attachment` | object | 否 | — |
| `QueryTypeV2.output.array` | object | 否 | — |
| `QueryTypeV2.output.array.subType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.output.objectSet` | object | 否 | — |
| `QueryTypeV2.output.objectSet.objectApiName` | string | 否 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.output.objectSet.objectTypeApiName` | string | 否 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.output.twoDimensionalAggregation` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.output.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `QueryTypeV2.output.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `QueryTypeV2.output.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `QueryTypeV2.output.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query. |
| `QueryTypeV2.output.timestamp` | object | 否 | — |
| `QueryTypeV2.output.set` | object | 否 | — |
| `QueryTypeV2.output.set.subType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.output.void` | object | 否 | — |
| `QueryTypeV2.output.entrySet` | object | 否 | — |
| `QueryTypeV2.output.entrySet.keyType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.output.entrySet.valueType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.output.double` | object | 否 | — |
| `QueryTypeV2.output.union` | object | 否 | — |
| `QueryTypeV2.output.union.unionTypes` | list<QueryDataType> | 否 | — |
| `QueryTypeV2.output.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.output.boolean` | object | 否 | — |
| `QueryTypeV2.output.mediaReference` | object | 否 | — |
| `QueryTypeV2.output.null` | object | 否 | — |
| `QueryTypeV2.output.interfaceObjectSet` | object | 否 | — |
| `QueryTypeV2.output.interfaceObjectSet.interfaceTypeApiName` | string | 是 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.output.object` | object | 否 | — |
| `QueryTypeV2.output.object.objectApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.output.object.objectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.rid` | string | 是 | The unique resource identifier of a Function, useful for interacting with other Foundry APIs. |
| `QueryTypeV2.version` | string | 是 | The version of the given Function, written `<major>.<minor>.<patch>-<tag>`, where `-<tag>` is optional.<br>Examples: `1.2.3`, `1.2.3-rc1`. |
| `QueryTypeV2.typeReferences` | map | 否 | — |
| `QueryTypeV2.typeReferences.TypeReferenceIdentifier` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query. |
| `QueryTypeV2.typeReferences.QueryDataType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.typeReferences.QueryDataType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.interfaceObject` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.interfaceObject.interfaceTypeApiName` | string | 否 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.typeReferences.QueryDataType.struct` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.struct.fields` | list<QueryStructField> | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.struct.fields.QueryStructField` | object | 是 | — |
| `QueryTypeV2.typeReferences.QueryDataType.struct.fields.QueryStructField.name` | string | 是 | The name of a field in a `Struct`. |
| `QueryTypeV2.typeReferences.QueryDataType.struct.fields.QueryStructField.fieldType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.typeReferences.QueryDataType.string` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.integer` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.string` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.integer` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType` | object | 是 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.string` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.integer` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.threeDimensionalAggregation.valueType.valueType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.float` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.long` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.unsupported` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.unsupported.unsupportedType` | string | 是 | — |
| `QueryTypeV2.typeReferences.QueryDataType.unsupported.params` | map | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.unsupported.params.UnsupportedTypeParamKey` | string | 是 | — |
| `QueryTypeV2.typeReferences.QueryDataType.unsupported.params.UnsupportedTypeParamValue` | string | 是 | — |
| `QueryTypeV2.typeReferences.QueryDataType.attachment` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.array` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.array.subType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.typeReferences.QueryDataType.objectSet` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.objectSet.objectApiName` | string | 否 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.typeReferences.QueryDataType.objectSet.objectTypeApiName` | string | 否 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.boolean` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.string` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType` | union | 是 | A union of all the types supported by query aggregation ranges. |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.integer` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.range.subType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.integer` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.keyType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.valueType` | union | 是 | A union of all the types supported by query aggregation keys. |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.date` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.twoDimensionalAggregation.valueType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.typeReference` | object | 否 | A reference to a type that is defined in the `typeReferences` map of the enclosing Query.<br>This enables support for recursive type definitions where a type may reference itself. |
| `QueryTypeV2.typeReferences.QueryDataType.typeReference.typeId` | string | 是 | The unique identifier of a type reference. This identifier is used to look up the<br>type definition in the `typeReferences` map of the enclosing Query. |
| `QueryTypeV2.typeReferences.QueryDataType.timestamp` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.set` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.set.subType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.typeReferences.QueryDataType.void` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.entrySet` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.entrySet.keyType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.typeReferences.QueryDataType.entrySet.valueType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.typeReferences.QueryDataType.double` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.union` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.union.unionTypes` | list<QueryDataType> | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.union.unionTypes.QueryDataType` | union | 是 | A union of all the types supported by Ontology Query parameters or outputs. |
| `QueryTypeV2.typeReferences.QueryDataType.boolean` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.mediaReference` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.null` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.interfaceObjectSet` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.interfaceObjectSet.interfaceTypeApiName` | string | 是 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.typeReferences.QueryDataType.object` | object | 否 | — |
| `QueryTypeV2.typeReferences.QueryDataType.object.objectApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `QueryTypeV2.typeReferences.QueryDataType.object.objectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |

```json
{
  "apiName": "getEmployeesInCity",
  "displayName": "Get Employees in City",
  "description": "Gets all employees in a given city",
  "parameters": {
    "city": {
      "dataType": {
        "type": "string"
      },
      "description": "The city to search for employees in",
      "required": true
    }
  },
  "output": {
    "dataType": {
      "type": "array",
      "subType": {
        "type": "object",
        "objectApiName": "Employee"
      }
    },
    "required": true
  },
  "rid": "ri.function-registry.main.function.f05481407-1d67-4120-83b4-e3fed5305a29b",
  "version": "1.1.3-rc1"
}
```
