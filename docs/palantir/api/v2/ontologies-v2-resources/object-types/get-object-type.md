`GET /api/v2/ontologies/{ontology}/objectTypes/{objectType}`

Gets a specific object type with the given API name.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager**.<br>示例: `employee` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branch` | string | 否 | The Foundry branch to load the object type definition from. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `includeDatasources` | boolean | 否 | When set to `true`, the `datasources` field on the returned object type is populated with the<br>datasources backing it. Defaults to `false`. |

## Response

**ObjectTypeV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ObjectTypeV2` | object | 是 | Success response.<br>示例: `{"apiName":"employee","description":"A full-time or part-time employee of our firm","displayName":"Employee","status":"ACTIVE","primaryKey":"employeeId","properties":{"employeeId":{"dataType":{"type":"integer"},"rid":"ri.ontology.main.property.571d3d4d-150a-4dd4-b1a7-d16c1ed7d996"},"fullName":{"dataType":{"type":"string"},"rid":"ri.ontology.main.property.5721baa7-26d5-4ca8-b092-d47dcc724ab1"},"office":{"description":"The unique ID of the employee's primary assigned office","dataType":{"type":"string"},"rid":"ri.ontology.main.property.554fa8c4-3b6e-4d3f-adef-acc9f0f54633"},"startDate":{"description":"The date the employee was hired (most recently, if they were re-hired)","dataType":{"type":"date"},"rid":"ri.ontology.main.property.3b081417-fe68-4010-ade8-68b298116ed4"}},"rid":"ri.ontology.main.object-type.0381eda6-69bb-4cb7-8ba0-c6158e094a04"}` |
| `ObjectTypeV2.apiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.displayName` | string | 是 | The display name of the entity. |
| `ObjectTypeV2.status` | enum | 是 | The release status of the entity.<br>示例: `ACTIVE` |
| `ObjectTypeV2.description` | string | 否 | The description of the object type. |
| `ObjectTypeV2.pluralDisplayName` | string | 是 | The plural display name of the object type. |
| `ObjectTypeV2.icon` | union | 是 | A union currently only consisting of the BlueprintIcon (more icon types may be added in the future). |
| `ObjectTypeV2.icon.blueprint` | object | 否 | — |
| `ObjectTypeV2.icon.blueprint.color` | string | 是 | A hexadecimal color code. |
| `ObjectTypeV2.icon.blueprint.name` | string | 是 | The [name](https://blueprintjs.com/docs/#icons/icons-list) of the Blueprint icon.<br>Used to specify the Blueprint icon to represent the object type in a React app. |
| `ObjectTypeV2.primaryKey` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.properties` | map | 否 | A map of the properties of the object type. |
| `ObjectTypeV2.properties.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.properties.PropertyV2` | object | 是 | Details about some property of an object. |
| `ObjectTypeV2.properties.PropertyV2.description` | string | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.displayName` | string | 否 | The display name of the entity. |
| `ObjectTypeV2.properties.PropertyV2.dataType` | union | 是 | A union of all the types supported by Ontology Object properties. |
| `ObjectTypeV2.properties.PropertyV2.dataType.date` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes` | list<StructFieldType> | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType` | object | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType.apiName` | string | 是 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType.rid` | string | 是 | The unique resource identifier of a struct field, useful for interacting with other Foundry APIs. |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType.dataType` | union | 是 | A union of all the types supported by Ontology Object properties. |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType.typeClasses` | list<TypeClass> | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType.typeClasses.TypeClass` | object | 是 | Additional metadata that can be interpreted by user applications that interact with the Ontology |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType.typeClasses.TypeClass.kind` | string | 是 | A namespace for the type class. |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.structFieldTypes.StructFieldType.typeClasses.TypeClass.name` | string | 是 | The value of the type class. |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.mainValue` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.mainValue.mainValueType` | union | 是 | A union of all the types supported by Ontology Object properties. |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.mainValue.fields` | list<StructFieldApiName> | 否 | The fields which comprise the main value of the struct. |
| `ObjectTypeV2.properties.PropertyV2.dataType.struct.mainValue.fields.StructFieldApiName` | string | 是 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.properties.PropertyV2.dataType.string` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.byte` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.double` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.geopoint` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.geotimeSeriesReference` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.integer` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.float` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.geoshape` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.long` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.boolean` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.cipherText` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.cipherText.defaultCipherChannel` | string | 否 | An optional Cipher Channel RID which can be used for encryption updates to empty values.<br>示例: `ri.bellaso.main.cipher-channel.00000000-0000-0000-0000-000000000000` |
| `ObjectTypeV2.properties.PropertyV2.dataType.marking` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.marking.markingType` | enum | 否 | The kind of marking applied by a marking property type.<br>- `CBAC`: Classification-based access control markings.<br>- `MANDATORY`: Standard non-classification markings. Example - Organizations. |
| `ObjectTypeV2.properties.PropertyV2.dataType.attachment` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.mediaReference` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.timeseries` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.timeseries.itemType` | union | 是 | A union of the types supported by time series properties. |
| `ObjectTypeV2.properties.PropertyV2.dataType.timeseries.itemType.string` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.timeseries.itemType.double` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.timeseries.itemType.numericOrNonNumeric` | object | 否 | The time series property can either contain either numeric or non-numeric data. This enables mixed sensor types<br>where some sensor time series are numeric and others are categorical. A boolean property reference can be used<br>to determine if the series is numeric or non-numeric. Without this property, the series type can be either<br>numeric or non-numeric and must be inferred from the result of a time series query. |
| `ObjectTypeV2.properties.PropertyV2.dataType.timeseries.itemType.numericOrNonNumeric.isNonNumericPropertyTypeId` | string | 否 | The boolean property type ID specifying whether the series is numeric or non-numeric. If the value is true,<br>the series is non-numeric. |
| `ObjectTypeV2.properties.PropertyV2.dataType.array` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.array.subType` | union | 是 | A union of all the types supported by Ontology Object properties. |
| `ObjectTypeV2.properties.PropertyV2.dataType.array.reducers` | list<OntologyObjectArrayTypeReducer> | 否 | If non-empty, this property can be reduced to a single value of the subtype. The reducers are applied in<br>order to determine a winning value. The array can be loaded as a reduced value or as the full array in an<br>object set. |
| `ObjectTypeV2.properties.PropertyV2.dataType.array.reducers.OntologyObjectArrayTypeReducer` | object | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.array.reducers.OntologyObjectArrayTypeReducer.direction` | enum | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.array.reducers.OntologyObjectArrayTypeReducer.field` | string | 否 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.properties.PropertyV2.dataType.short` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector` | object | 否 | Represents a fixed size vector of floats. These can be used for vector similarity searches. |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.dimension` | integer | 是 | The dimension of the vector. |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.supportsSearchWith` | list<VectorSimilarityFunction> | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.supportsSearchWith.VectorSimilarityFunction` | object | 是 | The vector similarity function to support approximate nearest neighbors search. Will result in an index<br>specific for the function. |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.supportsSearchWith.VectorSimilarityFunction.value` | enum | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.embeddingModel` | union | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.embeddingModel.lms` | object | 否 | A model provided by Language Model Service. |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.embeddingModel.lms.value` | enum | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.embeddingModel.foundryLiveDeployment` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.embeddingModel.foundryLiveDeployment.rid` | string | 否 | The live deployment identifier. This rid is of the format 'ri.foundry-ml-live.main.live-deployment.<uuid>'. |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.embeddingModel.foundryLiveDeployment.inputParamName` | string | 否 | The name of the input parameter to the model which should contain the query string. |
| `ObjectTypeV2.properties.PropertyV2.dataType.vector.embeddingModel.foundryLiveDeployment.outputParamName` | string | 否 | The name of the output parameter to the model which should contain the computed embedding. |
| `ObjectTypeV2.properties.PropertyV2.dataType.decimal` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.dataType.decimal.precision` | integer | 否 | The total number of digits of the Decimal type. The maximum value is 38.<br>示例: `38` |
| `ObjectTypeV2.properties.PropertyV2.dataType.decimal.scale` | integer | 否 | The number of digits to the right of the decimal point. The maximum value is 38.<br>示例: `18` |
| `ObjectTypeV2.properties.PropertyV2.dataType.timestamp` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.rid` | string | 是 | The unique resource identifier of a property. |
| `ObjectTypeV2.properties.PropertyV2.status` | union | 否 | The status to indicate whether the PropertyType is either Experimental, Active, Deprecated, or Example. |
| `ObjectTypeV2.properties.PropertyV2.status.deprecated` | object | 否 | This status indicates that the PropertyType is reaching the end of its life and will be removed as per the<br>deadline specified. |
| `ObjectTypeV2.properties.PropertyV2.status.deprecated.message` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.status.deprecated.deadline` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.status.deprecated.replacedBy` | string | 否 | The unique resource identifier of a property. |
| `ObjectTypeV2.properties.PropertyV2.status.active` | object | 否 | This status indicates that the PropertyType will not change on short notice and should thus be safe to use in<br>user facing workflows. |
| `ObjectTypeV2.properties.PropertyV2.status.experimental` | object | 否 | This status indicates that the PropertyType is in development. |
| `ObjectTypeV2.properties.PropertyV2.status.example` | object | 否 | This status indicates that the PropertyType is an example. It is backed by notional data that should not be<br>used for actual workflows, but can be used to test those workflows. |
| `ObjectTypeV2.properties.PropertyV2.visibility` | enum | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueTypeApiName` | string | 否 | The name of the value type in the API in camelCase format. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting` | union | 否 | This feature is experimental and may change in a future release.<br>Comprehensive formatting configuration for displaying property values in user interfaces.<br>Supports different value types including numbers, dates, timestamps, booleans, and known Foundry types.<br>Each formatter type provides specific options tailored to that data type:<br>- Numbers: Support for percentages, currencies, units, scaling, and custom formatting<br>- Dates/Timestamps: Localized and custom formatting patterns<br>- Booleans: Custom true/false display text<br>- Known types: Special formatting for Foundry-specific identifiers |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.date` | object | 否 | Formatting configuration for date property values. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.date.format` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.date.format.stringFormat` | object | 否 | A strictly specified date format pattern. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.date.format.stringFormat.pattern` | string | 是 | A valid format string composed of date/time patterns. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.date.format.localizedFormat` | object | 否 | Predefined localized formatting options. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.date.format.localizedFormat.format` | enum | 是 | Localized date/time format types. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number` | object | 否 | Wrapper for numeric formatting options. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard` | object | 否 | Standard number formatting with configurable options.<br>This provides basic number formatting without any special units, scaling, or transformations. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions` | object | 是 | Base number formatting options that can be applied to all number formatters.<br>Controls precision, grouping, rounding, and notation. Consistent with JavaScript's Intl.NumberFormat.<br>Examples:<br>- useGrouping: true makes 1234567 display as "1,234,567"<br>- maximumFractionDigits: 2 makes 3.14159 display as "3.14"<br>- notation: SCIENTIFIC makes 1234 display as "1.234E3" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.useGrouping` | boolean | 否 | If true, show a locale-appropriate number grouping (e.g. thousands for en). |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.convertNegativeToParenthesis` | boolean | 否 | If true, wrap negative numbers in parentheses instead of a minus sign. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.minimumIntegerDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.minimumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.maximumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.minimumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.maximumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.notation` | enum | 否 | Number notation style options:<br>- STANDARD: Regular number display ("1,234")<br>- SCIENTIFIC: Scientific notation ("1.234E3")<br>- ENGINEERING: Engineering notation ("1.234E3")<br>- COMPACT: Compact notation ("1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standard.baseFormatOptions.roundingMode` | enum | 否 | Number rounding behavior:<br>- CEIL: Always round up (3.1 becomes 4)<br>- FLOOR: Always round down (3.9 becomes 3)<br>- ROUND_CLOSEST: Round to nearest (3.4 becomes 3, 3.6 becomes 4) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.duration` | object | 否 | Format numeric values representing time durations.<br>- Human readable: 3661 seconds displays as "1h 1m 1s"<br>- Timecode: 3661 seconds displays as "01:01:01" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.duration.formatStyle` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.duration.formatStyle.humanReadable` | object | 否 | Formats the duration as a human-readable written string. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.duration.formatStyle.humanReadable.showFullUnits` | boolean | 否 | Whether to show full or abbreviated time units. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.duration.formatStyle.timecode` | object | 否 | Formats the duration in a timecode format. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.duration.precision` | enum | 否 | Specifies the maximum precision to apply when formatting a duration. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.duration.baseValue` | enum | 是 | Specifies the unit of the input duration value. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.fixedValues` | object | 否 | Map integer values to custom human-readable strings.<br>Example: {1: "First", 2: "Second", 3: "Third"} would display 2 as "Second". |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.fixedValues.values` | map | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.fixedValues.values.FixedValuesMapKey` | integer | 是 | Integer key for fixed value mapping. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix` | object | 否 | Attach arbitrary text before and/or after the formatted number.<br>Example: prefix "USD " and postfix " total" displays as "USD 1,234.56 total" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions` | object | 是 | Base number formatting options that can be applied to all number formatters.<br>Controls precision, grouping, rounding, and notation. Consistent with JavaScript's Intl.NumberFormat.<br>Examples:<br>- useGrouping: true makes 1234567 display as "1,234,567"<br>- maximumFractionDigits: 2 makes 3.14159 display as "3.14"<br>- notation: SCIENTIFIC makes 1234 display as "1.234E3" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.useGrouping` | boolean | 否 | If true, show a locale-appropriate number grouping (e.g. thousands for en). |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.convertNegativeToParenthesis` | boolean | 否 | If true, wrap negative numbers in parentheses instead of a minus sign. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.minimumIntegerDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.minimumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.maximumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.minimumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.maximumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.notation` | enum | 否 | Number notation style options:<br>- STANDARD: Regular number display ("1,234")<br>- SCIENTIFIC: Scientific notation ("1.234E3")<br>- ENGINEERING: Engineering notation ("1.234E3")<br>- COMPACT: Compact notation ("1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.baseFormatOptions.roundingMode` | enum | 否 | Number rounding behavior:<br>- CEIL: Always round up (3.1 becomes 4)<br>- FLOOR: Always round down (3.9 becomes 3)<br>- ROUND_CLOSEST: Round to nearest (3.4 becomes 3, 3.6 becomes 4) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix` | object | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.prefix` | union | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.prefix.constant` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.prefix.constant.value` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.prefix.propertyType` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.prefix.propertyType.propertyApiName` | string | 是 | The API name of the PropertyType |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.postfix` | union | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.postfix.constant` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.postfix.constant.value` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.postfix.propertyType` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.affix.affix.postfix.propertyType.propertyApiName` | string | 是 | The API name of the PropertyType |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale` | object | 否 | Scale the numeric value by dividing by the specified factor and append an appropriate suffix.<br>- THOUSANDS: 1500 displays as "1.5K"<br>- MILLIONS: 2500000 displays as "2.5M"<br>- BILLIONS: 3200000000 displays as "3.2B" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.scaleType` | enum | 是 | Scale factor options for large numbers:<br>- THOUSANDS: Divide by 1,000 and add "K" suffix<br>- MILLIONS: Divide by 1,000,000 and add "M" suffix<br>- BILLIONS: Divide by 1,000,000,000 and add "B" suffix |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions` | object | 是 | Base number formatting options that can be applied to all number formatters.<br>Controls precision, grouping, rounding, and notation. Consistent with JavaScript's Intl.NumberFormat.<br>Examples:<br>- useGrouping: true makes 1234567 display as "1,234,567"<br>- maximumFractionDigits: 2 makes 3.14159 display as "3.14"<br>- notation: SCIENTIFIC makes 1234 display as "1.234E3" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.useGrouping` | boolean | 否 | If true, show a locale-appropriate number grouping (e.g. thousands for en). |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.convertNegativeToParenthesis` | boolean | 否 | If true, wrap negative numbers in parentheses instead of a minus sign. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.minimumIntegerDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.minimumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.maximumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.minimumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.maximumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.notation` | enum | 否 | Number notation style options:<br>- STANDARD: Regular number display ("1,234")<br>- SCIENTIFIC: Scientific notation ("1.234E3")<br>- ENGINEERING: Engineering notation ("1.234E3")<br>- COMPACT: Compact notation ("1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.scale.baseFormatOptions.roundingMode` | enum | 否 | Number rounding behavior:<br>- CEIL: Always round up (3.1 becomes 4)<br>- FLOOR: Always round down (3.9 becomes 3)<br>- ROUND_CLOSEST: Round to nearest (3.4 becomes 3, 3.6 becomes 4) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency` | object | 否 | Format numbers as currency values with proper symbols and styling.<br>Example: 1234.56 with currency "USD" displays as "USD 1,234.56" (standard) or "USD 1.2K" (compact) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions` | object | 是 | Base number formatting options that can be applied to all number formatters.<br>Controls precision, grouping, rounding, and notation. Consistent with JavaScript's Intl.NumberFormat.<br>Examples:<br>- useGrouping: true makes 1234567 display as "1,234,567"<br>- maximumFractionDigits: 2 makes 3.14159 display as "3.14"<br>- notation: SCIENTIFIC makes 1234 display as "1.234E3" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.useGrouping` | boolean | 否 | If true, show a locale-appropriate number grouping (e.g. thousands for en). |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.convertNegativeToParenthesis` | boolean | 否 | If true, wrap negative numbers in parentheses instead of a minus sign. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.minimumIntegerDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.minimumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.maximumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.minimumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.maximumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.notation` | enum | 否 | Number notation style options:<br>- STANDARD: Regular number display ("1,234")<br>- SCIENTIFIC: Scientific notation ("1.234E3")<br>- ENGINEERING: Engineering notation ("1.234E3")<br>- COMPACT: Compact notation ("1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.baseFormatOptions.roundingMode` | enum | 否 | Number rounding behavior:<br>- CEIL: Always round up (3.1 becomes 4)<br>- FLOOR: Always round down (3.9 becomes 3)<br>- ROUND_CLOSEST: Round to nearest (3.4 becomes 3, 3.6 becomes 4) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.style` | enum | 是 | Currency rendering style options:<br>- STANDARD: Full currency formatting (e.g., "USD 1,234.56")<br>- COMPACT: Abbreviated currency formatting (e.g., "USD 1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.currencyCode` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.currencyCode.constant` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.currencyCode.constant.value` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.currencyCode.propertyType` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.currency.currencyCode.propertyType.propertyApiName` | string | 是 | The API name of the PropertyType |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit` | object | 否 | Format numbers with standard units supported by Intl.NumberFormat.<br>Examples: "meter", "kilogram", "celsius", "percent"<br>Input: 25 with unit "celsius" displays as "25 degrees C" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions` | object | 是 | Base number formatting options that can be applied to all number formatters.<br>Controls precision, grouping, rounding, and notation. Consistent with JavaScript's Intl.NumberFormat.<br>Examples:<br>- useGrouping: true makes 1234567 display as "1,234,567"<br>- maximumFractionDigits: 2 makes 3.14159 display as "3.14"<br>- notation: SCIENTIFIC makes 1234 display as "1.234E3" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.useGrouping` | boolean | 否 | If true, show a locale-appropriate number grouping (e.g. thousands for en). |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.convertNegativeToParenthesis` | boolean | 否 | If true, wrap negative numbers in parentheses instead of a minus sign. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.minimumIntegerDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.minimumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.maximumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.minimumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.maximumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.notation` | enum | 否 | Number notation style options:<br>- STANDARD: Regular number display ("1,234")<br>- SCIENTIFIC: Scientific notation ("1.234E3")<br>- ENGINEERING: Engineering notation ("1.234E3")<br>- COMPACT: Compact notation ("1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.baseFormatOptions.roundingMode` | enum | 否 | Number rounding behavior:<br>- CEIL: Always round up (3.1 becomes 4)<br>- FLOOR: Always round down (3.9 becomes 3)<br>- ROUND_CLOSEST: Round to nearest (3.4 becomes 3, 3.6 becomes 4) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.unit` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.unit.constant` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.unit.constant.value` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.unit.propertyType` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.standardUnit.unit.propertyType.propertyApiName` | string | 是 | The API name of the PropertyType |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit` | object | 否 | Format numbers with custom units not supported by standard formatting.<br>Use this for domain-specific units like "requests/sec", "widgets", etc.<br>Example: 1500 with unit "widgets" displays as "1,500 widgets" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions` | object | 是 | Base number formatting options that can be applied to all number formatters.<br>Controls precision, grouping, rounding, and notation. Consistent with JavaScript's Intl.NumberFormat.<br>Examples:<br>- useGrouping: true makes 1234567 display as "1,234,567"<br>- maximumFractionDigits: 2 makes 3.14159 display as "3.14"<br>- notation: SCIENTIFIC makes 1234 display as "1.234E3" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.useGrouping` | boolean | 否 | If true, show a locale-appropriate number grouping (e.g. thousands for en). |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.convertNegativeToParenthesis` | boolean | 否 | If true, wrap negative numbers in parentheses instead of a minus sign. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.minimumIntegerDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.minimumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.maximumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.minimumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.maximumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.notation` | enum | 否 | Number notation style options:<br>- STANDARD: Regular number display ("1,234")<br>- SCIENTIFIC: Scientific notation ("1.234E3")<br>- ENGINEERING: Engineering notation ("1.234E3")<br>- COMPACT: Compact notation ("1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.baseFormatOptions.roundingMode` | enum | 否 | Number rounding behavior:<br>- CEIL: Always round up (3.1 becomes 4)<br>- FLOOR: Always round down (3.9 becomes 3)<br>- ROUND_CLOSEST: Round to nearest (3.4 becomes 3, 3.6 becomes 4) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.unit` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.unit.constant` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.unit.constant.value` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.unit.propertyType` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.customUnit.unit.propertyType.propertyApiName` | string | 是 | The API name of the PropertyType |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio` | object | 否 | Display the value as a ratio with different scaling factors and suffixes:<br>- PERCENTAGE: Multiply by 100 and add "%" suffix (0.15 â "15%")<br>- PER_MILLE: Multiply by 1000 and add "â°" suffix (0.015 â "15â°")<br>- BASIS_POINTS: Multiply by 10000 and add "bps" suffix (0.0015 â "15bps") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.ratioType` | enum | 是 | Ratio format options for displaying proportional values:<br>- PERCENTAGE: Multiply by 100 and add "%" suffix<br>- PER_MILLE: Multiply by 1000 and add "â°" suffix<br>- BASIS_POINTS: Multiply by 10000 and add "bps" suffix |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions` | object | 是 | Base number formatting options that can be applied to all number formatters.<br>Controls precision, grouping, rounding, and notation. Consistent with JavaScript's Intl.NumberFormat.<br>Examples:<br>- useGrouping: true makes 1234567 display as "1,234,567"<br>- maximumFractionDigits: 2 makes 3.14159 display as "3.14"<br>- notation: SCIENTIFIC makes 1234 display as "1.234E3" |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.useGrouping` | boolean | 否 | If true, show a locale-appropriate number grouping (e.g. thousands for en). |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.convertNegativeToParenthesis` | boolean | 否 | If true, wrap negative numbers in parentheses instead of a minus sign. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.minimumIntegerDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.minimumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.maximumFractionDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.minimumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.maximumSignificantDigits` | integer | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.notation` | enum | 否 | Number notation style options:<br>- STANDARD: Regular number display ("1,234")<br>- SCIENTIFIC: Scientific notation ("1.234E3")<br>- ENGINEERING: Engineering notation ("1.234E3")<br>- COMPACT: Compact notation ("1.2K") |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.number.numberType.ratio.baseFormatOptions.roundingMode` | enum | 否 | Number rounding behavior:<br>- CEIL: Always round up (3.1 becomes 4)<br>- FLOOR: Always round down (3.9 becomes 3)<br>- ROUND_CLOSEST: Round to nearest (3.4 becomes 3, 3.6 becomes 4) |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.boolean` | object | 否 | Formatting configuration for boolean property values. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.boolean.valueIfTrue` | string | 是 | Value to display if this boolean is true |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.boolean.valueIfFalse` | string | 是 | Value to display if this boolean is false |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.knownType` | object | 否 | Formatting configuration for known Foundry types. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.knownType.knownType` | enum | 是 | Known Foundry types for specialized formatting:<br>- userOrGroupRid: Format as user or group<br>- resourceRid: Format as resource<br>- artifactGid: Format as artifact |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp` | object | 否 | Formatting configuration for timestamp property values. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.format` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.format.stringFormat` | object | 否 | A strictly specified date format pattern. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.format.stringFormat.pattern` | string | 是 | A valid format string composed of date/time patterns. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.format.localizedFormat` | object | 否 | Predefined localized formatting options. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.format.localizedFormat.format` | enum | 是 | Localized date/time format types. |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone.static` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone.static.zoneId` | union | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone.static.zoneId.constant` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone.static.zoneId.constant.value` | string | 是 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone.static.zoneId.propertyType` | object | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone.static.zoneId.propertyType.propertyApiName` | string | 是 | The API name of the PropertyType |
| `ObjectTypeV2.properties.PropertyV2.valueFormatting.timestamp.displayTimezone.user` | object | 否 | The user's local timezone. |
| `ObjectTypeV2.properties.PropertyV2.typeClasses` | list<TypeClass> | 否 | — |
| `ObjectTypeV2.properties.PropertyV2.typeClasses.TypeClass` | object | 是 | Additional metadata that can be interpreted by user applications that interact with the Ontology |
| `ObjectTypeV2.properties.PropertyV2.typeClasses.TypeClass.kind` | string | 是 | A namespace for the type class. |
| `ObjectTypeV2.properties.PropertyV2.typeClasses.TypeClass.name` | string | 是 | The value of the type class. |
| `ObjectTypeV2.properties.PropertyV2.dataConstraints` | object | 否 | Data constraints for a property type, including nullability information. |
| `ObjectTypeV2.properties.PropertyV2.dataConstraints.nullability` | enum | 否 | Indicates whether values in mapped datasources and values created through actions may be null. Null values may<br>still be observed for objects that are not present in the datasource mapping. |
| `ObjectTypeV2.rid` | string | 是 | The unique resource identifier of an object type, useful for interacting with other Foundry APIs. |
| `ObjectTypeV2.titleProperty` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.visibility` | enum | 否 | The suggested visibility of the object type. |
| `ObjectTypeV2.aliases` | list<string> | 否 | Alternative names (synonyms) for the object type, usable as search terms. This field is only populated on<br>the get-by-RID read paths (e.g. `getObjectTypeV2`); it is always empty on the `listObjectTypesV2` endpoint. |
| `ObjectTypeV2.datasources` | list<ObjectTypeDatasource> | 否 | The datasources backing this object type which the user has access to see. Only populated when the request<br>specifies `includeDatasources=true`. This list may be empty if the user doesn't have access to any<br>datasources. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource` | object | 是 | A datasource that supplies property values for an object type. Each object type can have one or more<br>datasources; together they back all of the object type's properties. The `definition` carries the RID of the<br>backing Foundry resource (for example, the dataset RID for a dataset-backed object type), enabling callers to<br>navigate from an object type to its backing data. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.rid` | string | 是 | Randomly generated identifier for an object type's datasource.<br>示例: `ri.ontology.main.datasource.6e1b3641-2d96-4e7c-9c0f-73f7d1f3a16f` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition` | union | 是 | The definition of an object type datasource, identifying the kind of Foundry resource that backs the object<br>type. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.timeSeries` | object | 否 | An object type datasource backed by a time series sync, providing values for time-dependent properties. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.timeSeries.timeSeriesSyncRid` | string | 是 | The RID identifying a time series sync. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.timeSeries.properties` | list<PropertyApiName> | 否 | The set of properties that are bound to the time series. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.timeSeries.properties.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.unsupported` | object | 否 | A datasource of a kind not yet exposed in the public API. The `unsupportedType` discriminator supplies the<br>underlying OMS variant so callers can recognize known but unmodelled cases (e.g., derived properties). Variants<br>the adapter does not recognise at all are returned with an `"unknown"` discriminator. The `properties` list<br>enumerates the property API names this datasource backs. The `properties` will be empty for `"unknown"`<br>datasources. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.unsupported.unsupportedType` | string | 是 | A short, stable discriminator naming the underlying OMS variant. E.g., `"derivedProperties"` for<br>derived-properties datasources or `"unknown"` for variants the adapter does not recognize. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.unsupported.properties` | list<PropertyApiName> | 否 | The property API names that this datasource backs. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.unsupported.properties.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView` | object | 否 | An object type datasource backed by a Foundry restricted view. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.restrictedViewRid` | string | 是 | The RID of a Foundry restricted view.<br>示例: `ri.foundry.main.restricted-view.8b2adf7c-ba8e-4d33-9bd7-46cc1c321ad4` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping` | map | 否 | A mapping from property API name to a description of how that property is bound to the restricted view.<br>Properties whose mapping info cannot be modeled are omitted. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo` | union | 是 | Describes how a single object type property is bound to its backing tabular datasource. A property may be backed<br>by a single column, by a struct (with nested field mappings), or be edit-only (no backing column even though it<br>is permissioned to the tabular datasource). |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.struct` | object | 否 | A mapping from the backing column struct field names to a struct property's fields. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.struct.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.struct.fields` | map | 否 | — |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldName` | string | 是 | The name of a field in a `Struct`. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping` | object | 是 | A single struct field's mapping where `apiName` is the name of a struct field. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping.apiName` | string | 是 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.column` | object | 否 | A property bound to a single column in the backing datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.column.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.restrictedView.propertyMapping.PropertyTypeMappingInfo.editOnly` | object | 否 | A property on an object type that is permissioned to a tabular datasource, but the contents are only populated through Actions. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream` | object | 否 | An object type datasource backed by a Foundry stream. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.streamRid` | string | 是 | The RID of a Foundry stream.<br>示例: `ri.foundry.main.stream.f7e91c94-1cb2-4e34-8fdb-3da7c1a91e8b` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.branch` | string | 否 | The id of a datasource branch. Branch ids are user supplied strings, not RIDs.<br>示例: `master` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping` | map | 否 | A mapping from property API name to a description of how that property is bound to the stream. Properties<br>whose mapping info cannot be modeled are omitted. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo` | union | 是 | Describes how a single object type property is bound to its backing tabular datasource. A property may be backed<br>by a single column, by a struct (with nested field mappings), or be edit-only (no backing column even though it<br>is permissioned to the tabular datasource). |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.struct` | object | 否 | A mapping from the backing column struct field names to a struct property's fields. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.struct.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.struct.fields` | map | 否 | — |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldName` | string | 是 | The name of a field in a `Struct`. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping` | object | 是 | A single struct field's mapping where `apiName` is the name of a struct field. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping.apiName` | string | 是 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.column` | object | 否 | A property bound to a single column in the backing datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.column.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.stream.propertyMapping.PropertyTypeMappingInfo.editOnly` | object | 否 | A property on an object type that is permissioned to a tabular datasource, but the contents are only populated through Actions. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.mediaSetView` | object | 否 | An object type datasource backed by a Foundry media set view, providing media for media reference properties. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.mediaSetView.mediaSetRid` | string | 是 | The Resource Identifier (RID) of a Media Set in Foundry. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.mediaSetView.mediaSetViewRid` | string | 是 | The Resource Identifier (RID) of a single View of a Media Set. A Media Set View is an independent collection of Media Items. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.mediaSetView.properties` | list<PropertyApiName> | 否 | The set of properties that are bound to the media view. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.mediaSetView.properties.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct` | object | 否 | An object type datasource backed by a direct-write source. Property values are written directly to the<br>datasource rather than being read from a separate Foundry resource. Unlike an edits-only datasource, a direct<br>datasource has a backing source that values are written to by some writer. An edits-only datasource has no<br>backing source at all and its properties are populated solely via Actions. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.directSourceRid` | string | 是 | The RID of a direct-write source backing an object type.<br>示例: `ri.ontology.main.direct-source.6e1b3641-2d96-4e7c-9c0f-73f7d1f3a16f` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping` | map | 否 | A mapping from property API name to a description of how that property is bound to the direct datasource.<br>Properties whose mapping info cannot be modeled are omitted. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo` | union | 是 | Describes how a single object type property is bound to its backing tabular datasource. A property may be backed<br>by a single column, by a struct (with nested field mappings), or be edit-only (no backing column even though it<br>is permissioned to the tabular datasource). |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.struct` | object | 否 | A mapping from the backing column struct field names to a struct property's fields. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.struct.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.struct.fields` | map | 否 | — |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldName` | string | 是 | The name of a field in a `Struct`. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping` | object | 是 | A single struct field's mapping where `apiName` is the name of a struct field. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping.apiName` | string | 是 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.column` | object | 否 | A property bound to a single column in the backing datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.column.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.direct.propertyMapping.PropertyTypeMappingInfo.editOnly` | object | 否 | A property on an object type that is permissioned to a tabular datasource, but the contents are only populated through Actions. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.geotimeSeries` | object | 否 | An object type datasource backed by a Geotime series integration, providing values for Geotime series reference<br>properties. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.geotimeSeries.geotimeSeriesIntegrationRid` | string | 是 | The unique resource identifier of a geotime integration. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.geotimeSeries.properties` | list<PropertyApiName> | 否 | The set of properties that are bound to the Geotime series. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.geotimeSeries.properties.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.editsOnly` | object | 否 | An object type datasource that is not backed by any external Foundry resource. All properties on the object type<br>can only be populated via Actions. Other datasources have edit only *properties*, which are permissioned to the<br>backing tabular datasource. This datasource has no backing tabular datasource and is a true edit only object<br>type. Note that this datasource type is incompatible with any other datasource and all the properties on the<br>object type are backed by it. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset` | object | 否 | An object type datasource backed by a Foundry dataset. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.datasetRid` | string | 是 | The Resource Identifier (RID) of a Dataset.<br>示例: `ri.foundry.main.dataset.c26f11c8-cdb3-4f44-9f5d-9816ea1c82da` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.branch` | string | 否 | The id of a datasource branch. Branch ids are user supplied strings, not RIDs.<br>示例: `master` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping` | map | 否 | A mapping from property API name to a description of how that property is bound to the dataset. Properties<br>whose mapping info cannot be modeled are omitted. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo` | union | 是 | Describes how a single object type property is bound to its backing tabular datasource. A property may be backed<br>by a single column, by a struct (with nested field mappings), or be edit-only (no backing column even though it<br>is permissioned to the tabular datasource). |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.struct` | object | 否 | A mapping from the backing column struct field names to a struct property's fields. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.struct.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.struct.fields` | map | 否 | — |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldName` | string | 是 | The name of a field in a `Struct`. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping` | object | 是 | A single struct field's mapping where `apiName` is the name of a struct field. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping.apiName` | string | 是 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.column` | object | 否 | A property bound to a single column in the backing datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.column.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.dataset.propertyMapping.PropertyTypeMappingInfo.editOnly` | object | 否 | A property on an object type that is permissioned to a tabular datasource, but the contents are only populated through Actions. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table` | object | 否 | An object type datasource backed by a Foundry table. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.tableRid` | string | 是 | The RID of a Foundry table.<br>示例: `ri.tables.main.table.beb574f7-019c-4a01-abd1-ff3c97bfaec8` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.branch` | string | 否 | The id of a datasource branch. Branch ids are user supplied strings, not RIDs.<br>示例: `master` |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping` | map | 否 | A mapping from property API name to a description of how that property is bound to the table. Properties<br>whose mapping info cannot be modeled are omitted. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo` | union | 是 | Describes how a single object type property is bound to its backing tabular datasource. A property may be backed<br>by a single column, by a struct (with nested field mappings), or be edit-only (no backing column even though it<br>is permissioned to the tabular datasource). |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.struct` | object | 否 | A mapping from the backing column struct field names to a struct property's fields. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.struct.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.struct.fields` | map | 否 | — |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldName` | string | 是 | The name of a field in a `Struct`. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping` | object | 是 | A single struct field's mapping where `apiName` is the name of a struct field. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.struct.fields.StructFieldPropertyMapping.apiName` | string | 是 | The name of a struct field in the Ontology. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.column` | object | 否 | A property bound to a single column in the backing datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.column.column` | string | 是 | The name of a column in a tabular datasource. |
| `ObjectTypeV2.datasources.ObjectTypeDatasource.definition.table.propertyMapping.PropertyTypeMappingInfo.editOnly` | object | 否 | A property on an object type that is permissioned to a tabular datasource, but the contents are only populated through Actions. |

```json
{
  "apiName": "employee",
  "description": "A full-time or part-time employee of our firm",
  "displayName": "Employee",
  "status": "ACTIVE",
  "primaryKey": "employeeId",
  "properties": {
    "employeeId": {
      "dataType": {
        "type": "integer"
      },
      "rid": "ri.ontology.main.property.571d3d4d-150a-4dd4-b1a7-d16c1ed7d996"
    },
    "fullName": {
      "dataType": {
        "type": "string"
      },
      "rid": "ri.ontology.main.property.5721baa7-26d5-4ca8-b092-d47dcc724ab1"
    },
    "office": {
      "description": "The unique ID of the employee's primary assigned office",
      "dataType": {
        "type": "string"
      },
      "rid": "ri.ontology.main.property.554fa8c4-3b6e-4d3f-adef-acc9f0f54633"
    },
    "startDate": {
      "description": "The date the employee was hired (most recently, if they were re-hired)",
      "dataType": {
        "type": "date"
      },
      "rid": "ri.ontology.main.property.3b081417-fe68-4010-ade8-68b298116ed4"
    }
  },
  "rid": "ri.ontology.main.object-type.0381eda6-69bb-4cb7-8ba0-c6158e094a04"
}
```
