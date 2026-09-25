`POST /api/v2/ontologies/{ontology}/objectSets/loadObjectsMultipleObjectTypes`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Load the ontology objects present in the `ObjectSet` from the provided object set definition. The resulting 
objects may be scoped to an object type, in which all the selected properties on the object type are returned, or scoped 
to an interface, in which only the object type properties that implement the properties of any interfaces in its 
scope are returned. For objects that are scoped to an interface in the result, a mapping from interface to 
object implementation is returned in order to interpret the objects as the interfaces that they implement.

For Object Storage V1 backed objects, this endpoint returns a maximum of 10,000 objects. After 10,000 objects have been returned and if more objects
are available, attempting to load another page will result in an `ObjectsExceededLimit` error being returned. There is no limit on Object Storage V2 backed objects.

Note that null value properties will not be returned. In addition, property metadata (rid, apiName, and primaryKey)
will be prefixed with '$' instead of '__' as is the case in `loadObjects`.

Vector properties will not be returned unless included in the `select` parameter.

The `pageSize` parameter is a maximum. A page may contain fewer objects than requested if the
objects in the page are large enough to reach an internal memory limit; this does not indicate
that there are no more results. As long as the response contains a `nextPageToken`, the remaining
objects can be retrieved by requesting subsequent pages.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The package version of the generated SDK. |
| `branch` | string | 否 | The Foundry branch to load the object set for multiple object types. If not specified, the default branch is used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `transactionId` | string | 否 | The ID of an Ontology transaction to read from.<br>Transactions are an experimental feature and all workflows may not be supported. |
| `scenarioRid` | string | 否 | The resource identifier of an ontology scenario to load the object set from.<br>示例: `ri.actions..scenario.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |
| `executeInMemoryOnly` | boolean | 否 | If true, the request fails with an error when it cannot be computed in-memory.<br>Use this to opt into fast failure on requests that would otherwise require<br>heavier computation.<br>Defaults to false. |

## Request body

```json
{
  "objectSet": {
    "type": "base",
    "objectType": "Employee"
  },
  "pageSize": 10000,
  "pageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z"
}
```

## Response

**LoadObjectSetV2MultipleObjectTypesResponse**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `LoadObjectSetV2MultipleObjectTypesResponse` | object | 是 | Success response.<br>示例: `{"data":[{"$rid":"ri.phonograph2-objects.main.object.5b5dbc28-7f05-4e83-a33a-1e5b851","$primaryKey":50030,"$apiName":"Employee","employeeId":50030,"employeeName":"John"},{"$rid":"ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b61","$primaryKey":20090,"$apiName":"Employee","employeeId":20090,"employeeName":"John"},{"$rid":"ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b71","$primaryKey":23,"$apiName":"Athlete","jerseyNumber":23,"firstName":"Michael"}],"totalCount":"3,","interfaceToObjectTypeMappings":{"Person":{"Employee":{"first":"employeeName","identifier":"employeeId"},"Athlete":{"first":"firstName","identifier":"jerseyNumber"}}}}` |
| `LoadObjectSetV2MultipleObjectTypesResponse.data` | list<OntologyObjectV2> | 否 | The list of objects in the current page. |
| `LoadObjectSetV2MultipleObjectTypesResponse.data.OntologyObjectV2` | map | 是 | Represents an object in the Ontology. |
| `LoadObjectSetV2MultipleObjectTypesResponse.data.OntologyObjectV2.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.data.OntologyObjectV2.PropertyValue` | any | 是 | Represents the value of a property in the following format.<br>\| Type                                                                                                                      \| JSON encoding                                               \| Example                                                                                            \|<br>\|---------------------------------------------------------------------------------------------------------------------------\|-------------------------------------------------------------\|----------------------------------------------------------------------------------------------------\|<br>\| Array                                                                                                                     \| array                                                       \| `["alpha", "bravo", "charlie"]`                                                                    \|<br>\| [Attachment](/docs/foundry/api/v2/ontologies-v2-resources/attachment-properties/attachment-property-basics/)              \| JSON encoded `AttachmentProperty` object                    \| `{"rid":"ri.blobster.main.attachment.2f944bae-5851-4204-8615-920c969a9f2e"}`                       \|<br>\| Boolean                                                                                                                   \| boolean                                                     \| `true`                                                                                             \|<br>\| Byte                                                                                                                      \| number                                                      \| `31`                                                                                               \|<br>\| CipherText                                                                                                                \| string                                                      \| `"CIPHER::ri.bellaso.main.cipher-channel.e414ab9e-b606-499a-a0e1-844fa296ba7e::unzjs3VifsTxuIpf1fH1CJ7OaPBr2bzMMdozPaZJtCii8vVG60yXIEmzoOJaEl9mfFFe::CIPHER"`                                                                                                                                                                                        \|<br>\| Date                                                                                                                      \| ISO 8601 extended local date string                         \| `"2021-05-01"`                                                                                     \|<br>\| Decimal                                                                                                                   \| string                                                      \| `"2.718281828"`                                                                                    \|<br>\| Double                                                                                                                    \| number                                                      \| `3.14159265`                                                                                       \|<br>\| Float                                                                                                                     \| number                                                      \| `3.14159265`                                                                                       \|<br>\| GeoPoint                                                                                                                  \| geojson                                                     \| `{"type":"Point","coordinates":[102.0,0.5]}`                                                       \|<br>\| GeoShape                                                                                                                  \| geojson                                                     \| `{"type":"LineString","coordinates":[[102.0,0.0],[103.0,1.0],[104.0,0.0],[105.0,1.0]]}`            \|<br>\| Integer                                                                                                                   \| number                                                      \| `238940`                                                                                           \|<br>\| Long                                                                                                                      \| string                                                      \| `"58319870951433"`                                                                                 \|<br>\| [MediaReference](/docs/foundry/api/v2/ontologies-v2-resources/media-reference-properties/media-reference-property-basics/)\| JSON encoded `MediaReference` object                        \| `{"mimeType":"application/pdf","reference":{"type":"mediaSetViewItem","mediaSetViewItem":{"mediaSetRid":"ri.mio.main.media-set.4153d42f-ca4b-4e42-8ca5-8e6aa7edb642","mediaSetViewRid":"ri.mio.main.view.82a798ad-d637-4595-acc6-987bcf16629b","mediaItemRid":"ri.mio.main.media-item.001ec98b-1620-4814-9e17-8e9c4e536225"}}}`                       \|<br>\| Secured Property Value                                                                                                    \| JSON encoded `SecuredPropertyValue` object                  \| `{"value": 10, "propertySecurityIndex" : 5}`                                                       \|<br>\| Short                                                                                                                     \| number                                                      \| `8739`                                                                                             \|<br>\| String                                                                                                                    \| string                                                      \| `"Call me Ishmael"`                                                                                \|<br>\| Struct                                                                                                                    \| JSON object of struct field API name -> value               \| {"firstName": "Alex", "lastName": "Karp"}                                                          \|<br>\| Timestamp                                                                                                                 \| ISO 8601 extended offset date-time string in UTC zone       \| `"2021-01-04T05:00:00Z"`                                                                           \|<br>\| [Timeseries](/docs/foundry/api/v2/ontologies-v2-resources/time-series-properties/time-series-property-basics/)            \| JSON encoded `TimeseriesProperty` object or seriesId string \| `{"seriesId": "wellPressureSeriesId", "syncRid": ri.time-series-catalog.main.sync.04f5ac1f-91bf-44f9-a51f-4f34e06e42df"}` or `{"templateRid": "ri.codex-emu.main.template.367cac64-e53b-4653-b111-f61856a63df9", "templateVersion": "0.0.0"}` or `"wellPressureSeriesId"`\|                                                                           \|<br>\| Vector                                                                                                                    \| array                                                       \| `[0.1, 0.3, 0.02, 0.05 , 0.8, 0.4]`                                                                \|<br>Note that for backwards compatibility, the Boolean, Byte, Double, Float, Integer, and Short types can also be encoded as JSON strings. |
| `LoadObjectSetV2MultipleObjectTypesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `LoadObjectSetV2MultipleObjectTypesResponse.totalCount` | string | 是 | The total number of items across all pages.<br>示例: `7587` |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappings` | map | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappings.InterfaceTypeApiName` | string | 是 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappings.InterfaceToObjectTypeMappings` | map | 是 | Map from object type to the interface-to-object-type mapping for that object type. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappings.InterfaceToObjectTypeMappings.ObjectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappings.InterfaceToObjectTypeMappings.InterfaceToObjectTypeMapping` | map | 是 | Represents an implementation of an interface (the mapping of interface property to local property). |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappings.InterfaceToObjectTypeMappings.InterfaceToObjectTypeMapping.SharedPropertyTypeApiName` | string | 是 | The name of the shared property type in the API in lowerCamelCase format. To find the API name for your<br>shared property type, use the `List shared property types` endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappings.InterfaceToObjectTypeMappings.InterfaceToObjectTypeMapping.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2` | map | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceTypeApiName` | string | 是 | The name of the interface type in the API in UpperCamelCase format. To find the API name for your interface<br>type, use the `List interface types` endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2` | map | 是 | Map from object type to the interface property implementations of that object type. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.ObjectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2` | map | 是 | Represents an implementation of an interface (the mapping of interface property to how it is implemented. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyApiName` | string | 是 | The name of the interface property type in the API in lowerCamelCase format. To find the API name for your<br>interface property type, use the `List interface types` endpoint and check the `allPropertiesV2` field or check<br>the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation` | union | 是 | Describes how an object type implements an interface property. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structFieldImplementation` | object | 否 | An implementation of an interface property via the field of a local struct property. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structFieldImplementation.structFieldOfProperty` | object | 是 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structFieldImplementation.structFieldOfProperty.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structFieldImplementation.structFieldOfProperty.structFieldApiName` | string | 是 | The name of a struct field in the Ontology. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation` | object | 否 | An implementation of a struct interface property via a local struct property. Specifies a mapping of interface<br>struct fields to local struct fields or properties. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping` | map | 否 | An implementation of a struct interface property via a local struct property. Specifies a mapping of interface<br>struct fields to local struct fields or properties. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping.StructFieldApiName` | string | 是 | The name of a struct field in the Ontology. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation` | union | 是 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.structFieldOfProperty` | object | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.structFieldOfProperty.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.structFieldOfProperty.structFieldApiName` | string | 是 | The name of a struct field in the Ontology. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.property` | object | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.property.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.localPropertyImplementation` | object | 否 | An implementation of an interface property via a local property. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.localPropertyImplementation.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation` | object | 否 | An implementation of an interface property via applying reducers on the nested implementation. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation` | union | 是 | Describes how an object type implements an interface property when a reducer is applied to it. Is missing a<br>reduced property implementation to prevent arbitrarily nested implementations. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structFieldImplementation` | object | 否 | An implementation of an interface property via the field of a local struct property. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structFieldImplementation.structFieldOfProperty` | object | 是 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structFieldImplementation.structFieldOfProperty.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structFieldImplementation.structFieldOfProperty.structFieldApiName` | string | 是 | The name of a struct field in the Ontology. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation` | object | 否 | An implementation of a struct interface property via a local struct property. Specifies a mapping of interface<br>struct fields to local struct fields or properties. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping` | map | 否 | An implementation of a struct interface property via a local struct property. Specifies a mapping of interface<br>struct fields to local struct fields or properties. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping.StructFieldApiName` | string | 是 | The name of a struct field in the Ontology. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation` | union | 是 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.structFieldOfProperty` | object | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.structFieldOfProperty.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.structFieldOfProperty.structFieldApiName` | string | 是 | The name of a struct field in the Ontology. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.property` | object | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.structImplementation.mapping.PropertyOrStructFieldOfPropertyImplementation.property.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.localPropertyImplementation` | object | 否 | An implementation of an interface property via a local property. |
| `LoadObjectSetV2MultipleObjectTypesResponse.interfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingsV2.InterfaceToObjectTypeMappingV2.InterfacePropertyTypeImplementation.reducedPropertyImplementation.implementation.localPropertyImplementation.propertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2MultipleObjectTypesResponse.computeUsage` | number | 否 | A measurement of compute usage expressed in [compute-seconds](/docs/foundry/resource-management/usage-types#compute-second). For more information, please refer to the [Usage types](/docs/foundry/resource-management/usage-types) documentation.<br>示例: `3.7` |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities` | list<PropertySecurities> | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities` | object | 是 | A disjunctive set of security results for a property value. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction` | list<PropertySecurity> | 否 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity` | union | 是 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary` | object | 否 | All marking requirements applicable to a property value. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.conjunctive` | list<MarkingId> | 否 | The conjunctive set of markings required to access the property value.<br>All markings from a conjunctive set must be met for access. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.conjunctive.MarkingId` | string | 是 | The id of a classification or mandatory marking. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.disjunctive` | list<array> | 否 | The disjunctive set of markings required to access the property value.<br>Disjunctive markings are represented as a conjunctive list of disjunctive sets.<br>The top-level set is a conjunction of sets, where each inner set should be<br>treated as a unit where any marking within the set can satisfy the set.<br>All sets within the top level set should be satisfied. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.disjunctive.array` | list<MarkingId> | 是 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.disjunctive.array.MarkingId` | string | 是 | The id of a classification or mandatory marking. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.containerConjunctive` | list<MarkingId> | 否 | The conjunctive set of markings for the container of this property value,<br>such as the project of a dataset. These markings may differ from the marking<br>on the actual property value, but still must be satisfied for accessing the property<br>All markings from a conjunctive set must be met for access. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.containerConjunctive.MarkingId` | string | 是 | The id of a classification or mandatory marking. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.containerDisjunctive` | list<array> | 否 | The disjunctive set of markings for the container of this property value,<br>such as the project of a dataset. These markings may differ from the marking<br>on the actual property value, but still must be satisfied for accessing the property<br>All markings from a conjunctive set must be met for access.<br>Disjunctive markings are represented as a conjunctive list of disjunctive sets.<br>The top-level set is a conjunction of sets, where each inner set should be<br>treated as a unit where any marking within the set can satisfy the set.<br>All sets within the top level set should be satisfied. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.containerDisjunctive.array` | list<MarkingId> | 是 | — |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.propertyMarkingSummary.containerDisjunctive.array.MarkingId` | string | 是 | The id of a classification or mandatory marking. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.unsupportedPolicy` | object | 否 | Indicates the property is backed by a restricted view that does not support property securities. |
| `LoadObjectSetV2MultipleObjectTypesResponse.propertySecurities.PropertySecurities.disjunction.PropertySecurity.errorComputingSecurity` | object | 否 | Indicates the server was not able to load the securities of the property. |

```json
{
  "data": [
    {
      "$rid": "ri.phonograph2-objects.main.object.5b5dbc28-7f05-4e83-a33a-1e5b851",
      "$primaryKey": 50030,
      "$apiName": "Employee",
      "employeeId": 50030,
      "employeeName": "John"
    },
    {
      "$rid": "ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b61",
      "$primaryKey": 20090,
      "$apiName": "Employee",
      "employeeId": 20090,
      "employeeName": "John"
    },
    {
      "$rid": "ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b71",
      "$primaryKey": 23,
      "$apiName": "Athlete",
      "jerseyNumber": 23,
      "firstName": "Michael"
    }
  ],
  "totalCount": "3,",
  "interfaceToObjectTypeMappings": {
    "Person": {
      "Employee": {
        "first": "employeeName",
        "identifier": "employeeId"
      },
      "Athlete": {
        "first": "firstName",
        "identifier": "jerseyNumber"
      }
    }
  }
}
```
