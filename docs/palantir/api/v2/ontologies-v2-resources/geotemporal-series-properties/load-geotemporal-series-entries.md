`POST /api/v2/ontologies/{ontology}/objects/{objectType}/{primaryKey}/geotemporalSeries/{property}/loadEntries`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Load the geotemporal series entries for a given object's geotemporal series reference property within the
specified time range.

Each entry in the response is a map of property names to values, following the same structure as
`OntologyObjectV2`. Use the `additionalProperties` field in the request to control which properties are included
in each entry depending on the underlying geotemporal integration.

Results are paginated. Use the `nextPageToken` from the response to retrieve subsequent pages.

:::callout{theme=warning title=Warning}
  Geotemporal series integrations with only "cold storage" enabled are not supported.
:::


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or check the<br>**Ontology Manager**.<br>示例: `airplane` |
| `primaryKey` | string | 是 | The primary key of the object with the geotemporal series property.<br>示例: `XYZ123` |
| `property` | string | 是 | The API name of the geotemporal series property. To find the API name for your property, check the<br>**Ontology Manager** or use the **Get object type** endpoint.<br>示例: `locationHistory` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package RID of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Request body

```json
{
  "range": {
    "startTime": "2020-01-01T00:00:00Z",
    "endTime": "2020-06-01T00:00:00Z"
  },
  "additionalProperties": [
    "speed",
    "heading"
  ],
  "pageSize": 100
}
```

## Response

**LoadGeotemporalSeriesResponse**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `LoadGeotemporalSeriesResponse` | object | 是 | Success response.<br>示例: `{"data":[{"time":"2020-03-06T12:00:00Z","position":{"type":"Point","coordinates":[-122.4194,37.7749]},"speed":120.5,"heading":275.3},{"time":"2020-03-06T13:00:00Z","position":{"type":"Point","coordinates":[-122.4095,37.7845]},"speed":95.0,"heading":180.0}],"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZQ"}` |
| `LoadGeotemporalSeriesResponse.data` | list<GeotemporalSeriesEntry> | 否 | — |
| `LoadGeotemporalSeriesResponse.data.GeotemporalSeriesEntry` | map | 是 | A single geotemporal data point. Each entry is a map from property API names to property values. Standard<br>entries include "time" (ISO 8601 timestamp) and "position" (GeoPoint), and may include additional geotemporal<br>series metadata fields such as speed, heading, or altitude. |
| `LoadGeotemporalSeriesResponse.data.GeotemporalSeriesEntry.PropertyApiName` | string | 是 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `LoadGeotemporalSeriesResponse.data.GeotemporalSeriesEntry.PropertyValue` | any | 是 | Represents the value of a property in the following format.<br>\| Type                                                                                                                      \| JSON encoding                                               \| Example                                                                                            \|<br>\|---------------------------------------------------------------------------------------------------------------------------\|-------------------------------------------------------------\|----------------------------------------------------------------------------------------------------\|<br>\| Array                                                                                                                     \| array                                                       \| `["alpha", "bravo", "charlie"]`                                                                    \|<br>\| [Attachment](/docs/foundry/api/v2/ontologies-v2-resources/attachment-properties/attachment-property-basics/)              \| JSON encoded `AttachmentProperty` object                    \| `{"rid":"ri.blobster.main.attachment.2f944bae-5851-4204-8615-920c969a9f2e"}`                       \|<br>\| Boolean                                                                                                                   \| boolean                                                     \| `true`                                                                                             \|<br>\| Byte                                                                                                                      \| number                                                      \| `31`                                                                                               \|<br>\| CipherText                                                                                                                \| string                                                      \| `"CIPHER::ri.bellaso.main.cipher-channel.e414ab9e-b606-499a-a0e1-844fa296ba7e::unzjs3VifsTxuIpf1fH1CJ7OaPBr2bzMMdozPaZJtCii8vVG60yXIEmzoOJaEl9mfFFe::CIPHER"`                                                                                                                                                                                        \|<br>\| Date                                                                                                                      \| ISO 8601 extended local date string                         \| `"2021-05-01"`                                                                                     \|<br>\| Decimal                                                                                                                   \| string                                                      \| `"2.718281828"`                                                                                    \|<br>\| Double                                                                                                                    \| number                                                      \| `3.14159265`                                                                                       \|<br>\| Float                                                                                                                     \| number                                                      \| `3.14159265`                                                                                       \|<br>\| GeoPoint                                                                                                                  \| geojson                                                     \| `{"type":"Point","coordinates":[102.0,0.5]}`                                                       \|<br>\| GeoShape                                                                                                                  \| geojson                                                     \| `{"type":"LineString","coordinates":[[102.0,0.0],[103.0,1.0],[104.0,0.0],[105.0,1.0]]}`            \|<br>\| Integer                                                                                                                   \| number                                                      \| `238940`                                                                                           \|<br>\| Long                                                                                                                      \| string                                                      \| `"58319870951433"`                                                                                 \|<br>\| [MediaReference](/docs/foundry/api/v2/ontologies-v2-resources/media-reference-properties/media-reference-property-basics/)\| JSON encoded `MediaReference` object                        \| `{"mimeType":"application/pdf","reference":{"type":"mediaSetViewItem","mediaSetViewItem":{"mediaSetRid":"ri.mio.main.media-set.4153d42f-ca4b-4e42-8ca5-8e6aa7edb642","mediaSetViewRid":"ri.mio.main.view.82a798ad-d637-4595-acc6-987bcf16629b","mediaItemRid":"ri.mio.main.media-item.001ec98b-1620-4814-9e17-8e9c4e536225"}}}`                       \|<br>\| Secured Property Value                                                                                                    \| JSON encoded `SecuredPropertyValue` object                  \| `{"value": 10, "propertySecurityIndex" : 5}`                                                       \|<br>\| Short                                                                                                                     \| number                                                      \| `8739`                                                                                             \|<br>\| String                                                                                                                    \| string                                                      \| `"Call me Ishmael"`                                                                                \|<br>\| Struct                                                                                                                    \| JSON object of struct field API name -> value               \| {"firstName": "Alex", "lastName": "Karp"}                                                          \|<br>\| Timestamp                                                                                                                 \| ISO 8601 extended offset date-time string in UTC zone       \| `"2021-01-04T05:00:00Z"`                                                                           \|<br>\| [Timeseries](/docs/foundry/api/v2/ontologies-v2-resources/time-series-properties/time-series-property-basics/)            \| JSON encoded `TimeseriesProperty` object or seriesId string \| `{"seriesId": "wellPressureSeriesId", "syncRid": ri.time-series-catalog.main.sync.04f5ac1f-91bf-44f9-a51f-4f34e06e42df"}` or `{"templateRid": "ri.codex-emu.main.template.367cac64-e53b-4653-b111-f61856a63df9", "templateVersion": "0.0.0"}` or `"wellPressureSeriesId"`\|                                                                           \|<br>\| Vector                                                                                                                    \| array                                                       \| `[0.1, 0.3, 0.02, 0.05 , 0.8, 0.4]`                                                                \|<br>Note that for backwards compatibility, the Boolean, Byte, Double, Float, Integer, and Short types can also be encoded as JSON strings. |
| `LoadGeotemporalSeriesResponse.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

```json
{
  "data": [
    {
      "time": "2020-03-06T12:00:00Z",
      "position": {
        "type": "Point",
        "coordinates": [
          -122.4194,
          37.7749
        ]
      },
      "speed": 120.5,
      "heading": 275.3
    },
    {
      "time": "2020-03-06T13:00:00Z",
      "position": {
        "type": "Point",
        "coordinates": [
          -122.4095,
          37.7845
        ]
      },
      "speed": 95.0,
      "heading": 180.0
    }
  ],
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZQ"
}
```
