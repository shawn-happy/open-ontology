`GET /api/v2/ontologies/{ontology}/objects/{objectType}`

Lists the objects for the given Ontology and object type.

Note that this endpoint does not guarantee consistency. Changes to the data could result in missing or
repeated objects in the response pages.

For Object Storage V1 backed objects, this endpoint returns a maximum of 10,000 objects. After 10,000 objects have been returned and if more objects
are available, attempting to load another page will result in an `ObjectsExceededLimit` error being returned. There is no limit on Object Storage V2 backed objects.

Each page may be smaller or larger than the requested page size. However, it
is guaranteed that if there are more results available, at least one result will be present
in the response.

Note that null value properties will not be returned.


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
| `pageSize` | integer | 否 | The desired size of the page to be returned. Defaults to 1,000.<br>See [page sizes](/docs/foundry/api/general/overview/paging/#page-sizes) for details. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `select` | list<SelectedPropertyApiName> | 否 | The properties of the object type that should be included in the response. Omit this parameter to get all<br>the properties. |
| `select.SelectedPropertyApiName` | string | 是 | By default, whenever an object is requested, all of its properties are returned, except for properties of the<br>following types:<br>- Vector<br>The response can be filtered to only include certain properties using the `properties` query parameter. Note<br>that ontology object set endpoints refer to this parameter as `select`.<br>Properties to include can be specified in one of two ways.<br>- A comma delimited list as the value for the `properties` query parameter<br>`properties={property1ApiName},{property2ApiName}`<br>- Multiple `properties` query parameters.<br>`properties={property1ApiName}&properties={property2ApiName}`<br>The primary key of the object will always be returned even if it wasn't specified in the `properties` values.<br>Unknown properties specified in the `properties` list will result in a `PropertiesNotFound` error.<br>To find the API name for your property, use the `Get object type` endpoint or check the **Ontology Manager**. |
| `orderBy` | string | 否 | A command representing the list of properties to order by. Properties should be delimited by commas and<br>prefixed by `p` or `properties`. The format expected format is<br>`orderBy=properties.{property}:{sortDirection},properties.{property}:{sortDirection}...`<br>By default, the ordering for a property is ascending, and this can be explicitly specified by appending<br>`:asc` (for ascending) or `:desc` (for descending).<br>Example: use `orderBy=properties.lastName:asc` to order by a single property,<br>`orderBy=properties.lastName,properties.firstName,properties.age:desc` to order by multiple properties.<br>You may also use the shorthand `p` instead of `properties` such as `orderBy=p.lastName:asc`. |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `excludeRid` | boolean | 否 | A flag to exclude the retrieval of the `__rid` property.<br>Setting this to true may improve performance of this endpoint for object types in OSV2. |
| `snapshot` | boolean | 否 | A flag to use snapshot consistency when paging.<br>Setting this to true will give you a consistent view from before you start paging through the results, ensuring you do not get duplicate or missing items.<br>Setting this to false will let new results enter as you page, but you may encounter duplicate or missing items.<br>This defaults to false if not specified, which means you will always get the latest results. |
| `branch` | string | 否 | The Foundry branch to list objects from. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `loadOntologyDefinedDerivedProperties` | boolean | 否 | A flag to load ontology-defined derived properties (OTDPs) in the response. Defaults to true.<br>Only applies when no explicit property selection is provided; when specific properties are<br>selected, this flag has no effect and the selected properties are always returned.<br>This feature is experimental and not yet generally available. |

## Response

**ListObjectsResponseV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListObjectsResponseV2` | object | 是 | Success response.<br>示例: `{"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z","data":[{"__rid":"ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b61","__primaryKey":50030,"__apiName":"Employee","id":50030,"firstName":"John","lastName":"Doe"},{"__rid":"ri.phonograph2-objects.main.object.dcd887d1-c757-4d7a-8619-71e6ec2c25ab","__primaryKey":20090,"__apiName":"Employee","id":20090,"firstName":"John","lastName":"Haymore"}]}` |
| `ListObjectsResponseV2.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `ListObjectsResponseV2.data` | list<OntologyObjectV2> | 否 | The list of objects in the current page. |
| `ListObjectsResponseV2.data.OntologyObjectV2` | map | 是 | Represents an object in the Ontology. |
| `ListObjectsResponseV2.data.OntologyObjectV2.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ListObjectsResponseV2.data.OntologyObjectV2.PropertyValue` | any | 是 | Represents the value of a property in the following format.<br>\| Type                                                                                                                      \| JSON encoding                                               \| Example                                                                                            \|<br>\|---------------------------------------------------------------------------------------------------------------------------\|-------------------------------------------------------------\|----------------------------------------------------------------------------------------------------\|<br>\| Array                                                                                                                     \| array                                                       \| `["alpha", "bravo", "charlie"]`                                                                    \|<br>\| [Attachment](/docs/foundry/api/v2/ontologies-v2-resources/attachment-properties/attachment-property-basics/)              \| JSON encoded `AttachmentProperty` object                    \| `{"rid":"ri.blobster.main.attachment.2f944bae-5851-4204-8615-920c969a9f2e"}`                       \|<br>\| Boolean                                                                                                                   \| boolean                                                     \| `true`                                                                                             \|<br>\| Byte                                                                                                                      \| number                                                      \| `31`                                                                                               \|<br>\| CipherText                                                                                                                \| string                                                      \| `"CIPHER::ri.bellaso.main.cipher-channel.e414ab9e-b606-499a-a0e1-844fa296ba7e::unzjs3VifsTxuIpf1fH1CJ7OaPBr2bzMMdozPaZJtCii8vVG60yXIEmzoOJaEl9mfFFe::CIPHER"`                                                                                                                                                                                        \|<br>\| Date                                                                                                                      \| ISO 8601 extended local date string                         \| `"2021-05-01"`                                                                                     \|<br>\| Decimal                                                                                                                   \| string                                                      \| `"2.718281828"`                                                                                    \|<br>\| Double                                                                                                                    \| number                                                      \| `3.14159265`                                                                                       \|<br>\| Float                                                                                                                     \| number                                                      \| `3.14159265`                                                                                       \|<br>\| GeoPoint                                                                                                                  \| geojson                                                     \| `{"type":"Point","coordinates":[102.0,0.5]}`                                                       \|<br>\| GeoShape                                                                                                                  \| geojson                                                     \| `{"type":"LineString","coordinates":[[102.0,0.0],[103.0,1.0],[104.0,0.0],[105.0,1.0]]}`            \|<br>\| Integer                                                                                                                   \| number                                                      \| `238940`                                                                                           \|<br>\| Long                                                                                                                      \| string                                                      \| `"58319870951433"`                                                                                 \|<br>\| [MediaReference](/docs/foundry/api/v2/ontologies-v2-resources/media-reference-properties/media-reference-property-basics/)\| JSON encoded `MediaReference` object                        \| `{"mimeType":"application/pdf","reference":{"type":"mediaSetViewItem","mediaSetViewItem":{"mediaSetRid":"ri.mio.main.media-set.4153d42f-ca4b-4e42-8ca5-8e6aa7edb642","mediaSetViewRid":"ri.mio.main.view.82a798ad-d637-4595-acc6-987bcf16629b","mediaItemRid":"ri.mio.main.media-item.001ec98b-1620-4814-9e17-8e9c4e536225"}}}`                       \|<br>\| Secured Property Value                                                                                                    \| JSON encoded `SecuredPropertyValue` object                  \| `{"value": 10, "propertySecurityIndex" : 5}`                                                       \|<br>\| Short                                                                                                                     \| number                                                      \| `8739`                                                                                             \|<br>\| String                                                                                                                    \| string                                                      \| `"Call me Ishmael"`                                                                                \|<br>\| Struct                                                                                                                    \| JSON object of struct field API name -> value               \| {"firstName": "Alex", "lastName": "Karp"}                                                          \|<br>\| Timestamp                                                                                                                 \| ISO 8601 extended offset date-time string in UTC zone       \| `"2021-01-04T05:00:00Z"`                                                                           \|<br>\| [Timeseries](/docs/foundry/api/v2/ontologies-v2-resources/time-series-properties/time-series-property-basics/)            \| JSON encoded `TimeseriesProperty` object or seriesId string \| `{"seriesId": "wellPressureSeriesId", "syncRid": ri.time-series-catalog.main.sync.04f5ac1f-91bf-44f9-a51f-4f34e06e42df"}` or `{"templateRid": "ri.codex-emu.main.template.367cac64-e53b-4653-b111-f61856a63df9", "templateVersion": "0.0.0"}` or `"wellPressureSeriesId"`\|                                                                           \|<br>\| Vector                                                                                                                    \| array                                                       \| `[0.1, 0.3, 0.02, 0.05 , 0.8, 0.4]`                                                                \|<br>Note that for backwards compatibility, the Boolean, Byte, Double, Float, Integer, and Short types can also be encoded as JSON strings. |
| `ListObjectsResponseV2.totalCount` | string | 是 | The total number of items across all pages.<br>示例: `7587` |

```json
{
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z",
  "data": [
    {
      "__rid": "ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b61",
      "__primaryKey": 50030,
      "__apiName": "Employee",
      "id": 50030,
      "firstName": "John",
      "lastName": "Doe"
    },
    {
      "__rid": "ri.phonograph2-objects.main.object.dcd887d1-c757-4d7a-8619-71e6ec2c25ab",
      "__primaryKey": 20090,
      "__apiName": "Employee",
      "id": 20090,
      "firstName": "John",
      "lastName": "Haymore"
    }
  ]
}
```
