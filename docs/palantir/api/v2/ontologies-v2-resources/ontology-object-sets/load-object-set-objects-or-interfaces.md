`POST /api/v2/ontologies/{ontology}/objectSets/loadObjectsOrInterfaces`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Load the ontology objects present in the `ObjectSet` from the provided object set definition. If the requested 
object set contains interfaces and the object can be viewed as an interface, it will contain the properties 
defined by the interface. If not, it will contain the properties defined by its object type. This allows directly
loading all objects of an interface where all objects are viewed as the interface, for example.

Note that the result object set cannot contain a mix of objects with "interface" properties and "object type"
properties. Attempting to load an object set like this will result in an error.

For Object Storage V1 backed objects, this endpoint returns a maximum of 10,000 objects. After 10,000 objects have been returned and if more objects
are available, attempting to load another page will result in an `ObjectsExceededLimit` error being returned. There is no limit on Object Storage V2 backed objects.

Note that null value properties will not be returned. In addition, property metadata (rid, apiName, and primaryKey)
will be prefixed with '$' instead of '__' as is the case in `/loadObjects`.

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
| `branch` | string | 否 | The Foundry branch to load the objects or interfaces from. If not specified, the default branch is used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `transactionId` | string | 否 | The ID of an Ontology transaction to read from.<br>Transactions are an experimental feature and all workflows may not be supported. |
| `scenarioRid` | string | 否 | The resource identifier of an ontology scenario to load the objects or interfaces from.<br>示例: `ri.actions..scenario.a1b2c3d4-e5f6-7890-abcd-ef1234567890` |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |
| `executeInMemoryOnly` | boolean | 否 | If true, the request fails with an error when it cannot be computed in-memory.<br>Use this to opt into fast failure on requests that would otherwise require<br>heavier computation.<br>Defaults to false. |

## Request body

```json
{
  "objectSet": {
    "type": "interfaceBase",
    "interfaceType": "Person"
  },
  "pageSize": 10000,
  "pageToken": "v1.VGhlcmUgaXMgc28gbXVjaCBsZWZ0IHRvIGJ1aWxkIC0gcGFsYW50aXIuY29tL2NhcmVlcnMv"
}
```

## Response

**LoadObjectSetV2ObjectsOrInterfacesResponse**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `LoadObjectSetV2ObjectsOrInterfacesResponse` | object | 是 | Success response.<br>示例: `{"data":[{"$rid":"ri.phonograph2-objects.main.object.5b5dbc28-7f05-4e83-a33a-1e5b851","$primaryKey":50030,"$objectTypeApiName":"Employee","$interfaceTypeApiName":"Person","personId":50030,"firstName":"John"},{"$rid":"ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b61","$primaryKey":20090,"$objectTypeApiName":"Employee","$interfaceTypeApiName":"Person","personID":20090,"firstName":"John"},{"$rid":"ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b71","$primaryKey":23,"$objectTypeApiName":"Athlete","$interfaceTypeApiName":"Person","personId":23,"firstName":"Michael"}],"totalCount":3}` |
| `LoadObjectSetV2ObjectsOrInterfacesResponse.data` | list<OntologyObjectV2> | 否 | The list of objects in the current page. |
| `LoadObjectSetV2ObjectsOrInterfacesResponse.data.OntologyObjectV2` | map | 是 | Represents an object in the Ontology. |
| `LoadObjectSetV2ObjectsOrInterfacesResponse.data.OntologyObjectV2.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadObjectSetV2ObjectsOrInterfacesResponse.data.OntologyObjectV2.PropertyValue` | any | 是 | Represents the value of a property in the following format.<br>\| Type                                                                                                                      \| JSON encoding                                               \| Example                                                                                            \|<br>\|---------------------------------------------------------------------------------------------------------------------------\|-------------------------------------------------------------\|----------------------------------------------------------------------------------------------------\|<br>\| Array                                                                                                                     \| array                                                       \| `["alpha", "bravo", "charlie"]`                                                                    \|<br>\| [Attachment](/docs/foundry/api/v2/ontologies-v2-resources/attachment-properties/attachment-property-basics/)              \| JSON encoded `AttachmentProperty` object                    \| `{"rid":"ri.blobster.main.attachment.2f944bae-5851-4204-8615-920c969a9f2e"}`                       \|<br>\| Boolean                                                                                                                   \| boolean                                                     \| `true`                                                                                             \|<br>\| Byte                                                                                                                      \| number                                                      \| `31`                                                                                               \|<br>\| CipherText                                                                                                                \| string                                                      \| `"CIPHER::ri.bellaso.main.cipher-channel.e414ab9e-b606-499a-a0e1-844fa296ba7e::unzjs3VifsTxuIpf1fH1CJ7OaPBr2bzMMdozPaZJtCii8vVG60yXIEmzoOJaEl9mfFFe::CIPHER"`                                                                                                                                                                                        \|<br>\| Date                                                                                                                      \| ISO 8601 extended local date string                         \| `"2021-05-01"`                                                                                     \|<br>\| Decimal                                                                                                                   \| string                                                      \| `"2.718281828"`                                                                                    \|<br>\| Double                                                                                                                    \| number                                                      \| `3.14159265`                                                                                       \|<br>\| Float                                                                                                                     \| number                                                      \| `3.14159265`                                                                                       \|<br>\| GeoPoint                                                                                                                  \| geojson                                                     \| `{"type":"Point","coordinates":[102.0,0.5]}`                                                       \|<br>\| GeoShape                                                                                                                  \| geojson                                                     \| `{"type":"LineString","coordinates":[[102.0,0.0],[103.0,1.0],[104.0,0.0],[105.0,1.0]]}`            \|<br>\| Integer                                                                                                                   \| number                                                      \| `238940`                                                                                           \|<br>\| Long                                                                                                                      \| string                                                      \| `"58319870951433"`                                                                                 \|<br>\| [MediaReference](/docs/foundry/api/v2/ontologies-v2-resources/media-reference-properties/media-reference-property-basics/)\| JSON encoded `MediaReference` object                        \| `{"mimeType":"application/pdf","reference":{"type":"mediaSetViewItem","mediaSetViewItem":{"mediaSetRid":"ri.mio.main.media-set.4153d42f-ca4b-4e42-8ca5-8e6aa7edb642","mediaSetViewRid":"ri.mio.main.view.82a798ad-d637-4595-acc6-987bcf16629b","mediaItemRid":"ri.mio.main.media-item.001ec98b-1620-4814-9e17-8e9c4e536225"}}}`                       \|<br>\| Secured Property Value                                                                                                    \| JSON encoded `SecuredPropertyValue` object                  \| `{"value": 10, "propertySecurityIndex" : 5}`                                                       \|<br>\| Short                                                                                                                     \| number                                                      \| `8739`                                                                                             \|<br>\| String                                                                                                                    \| string                                                      \| `"Call me Ishmael"`                                                                                \|<br>\| Struct                                                                                                                    \| JSON object of struct field API name -> value               \| {"firstName": "Alex", "lastName": "Karp"}                                                          \|<br>\| Timestamp                                                                                                                 \| ISO 8601 extended offset date-time string in UTC zone       \| `"2021-01-04T05:00:00Z"`                                                                           \|<br>\| [Timeseries](/docs/foundry/api/v2/ontologies-v2-resources/time-series-properties/time-series-property-basics/)            \| JSON encoded `TimeseriesProperty` object or seriesId string \| `{"seriesId": "wellPressureSeriesId", "syncRid": ri.time-series-catalog.main.sync.04f5ac1f-91bf-44f9-a51f-4f34e06e42df"}` or `{"templateRid": "ri.codex-emu.main.template.367cac64-e53b-4653-b111-f61856a63df9", "templateVersion": "0.0.0"}` or `"wellPressureSeriesId"`\|                                                                           \|<br>\| Vector                                                                                                                    \| array                                                       \| `[0.1, 0.3, 0.02, 0.05 , 0.8, 0.4]`                                                                \|<br>Note that for backwards compatibility, the Boolean, Byte, Double, Float, Integer, and Short types can also be encoded as JSON strings. |
| `LoadObjectSetV2ObjectsOrInterfacesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `LoadObjectSetV2ObjectsOrInterfacesResponse.totalCount` | string | 是 | The total number of items across all pages.<br>示例: `7587` |
| `LoadObjectSetV2ObjectsOrInterfacesResponse.transactionId` | string | 否 | The ID identifying a transaction. |

```json
{
  "data": [
    {
      "$rid": "ri.phonograph2-objects.main.object.5b5dbc28-7f05-4e83-a33a-1e5b851",
      "$primaryKey": 50030,
      "$objectTypeApiName": "Employee",
      "$interfaceTypeApiName": "Person",
      "personId": 50030,
      "firstName": "John"
    },
    {
      "$rid": "ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b61",
      "$primaryKey": 20090,
      "$objectTypeApiName": "Employee",
      "$interfaceTypeApiName": "Person",
      "personID": 20090,
      "firstName": "John"
    },
    {
      "$rid": "ri.phonograph2-objects.main.object.88a6fccb-f333-46d6-a07e-7725c5f18b71",
      "$primaryKey": 23,
      "$objectTypeApiName": "Athlete",
      "$interfaceTypeApiName": "Person",
      "personId": 23,
      "firstName": "Michael"
    }
  ],
  "totalCount": 3
}
```
