`GET /api/v2/ontologies/{ontology}/objectTypes/{objectType}/outgoingLinkTypes`

List the outgoing links for an object type.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The API name of the object type. To find the API name, use the **List object types** endpoint or<br>check the **Ontology Manager** application.<br>示例: `Flight` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branch` | string | 否 | The Foundry branch to load the outgoing link types from. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `pageSize` | integer | 否 | The desired size of the page to be returned. |
| `pageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |

## Response

**ListOutgoingLinkTypesResponseV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListOutgoingLinkTypesResponseV2` | object | 是 | Success response.<br>示例: `{"nextPageToken":"v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z","data":[{"apiName":"originAirport","objectTypeApiName":"Airport","cardinality":"ONE","foreignKeyPropertyApiName":"originAirportId"}]}` |
| `ListOutgoingLinkTypesResponseV2.nextPageToken` | string | 否 | The page token indicates where to start paging. This should be omitted from the first page's request.<br>To fetch the next page, clients should take the value from the `nextPageToken` field of the previous response<br>and use it to populate the `pageToken` field of the next request. |
| `ListOutgoingLinkTypesResponseV2.data` | list<LinkTypeSideV2> | 否 | The list of link type sides in the current page. |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2` | object | 是 | `foreignKeyPropertyApiName` is the API name of the foreign key on this object type. If absent, the link is<br>either a m2m link or the linked object has the foreign key and this object type has the primary key. |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2.apiName` | string | 是 | The name of the link type in the API. To find the API name for your Link Type, check the **Ontology Manager**<br>application. |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2.displayName` | string | 是 | The display name of the entity. |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2.status` | enum | 是 | The release status of the entity.<br>示例: `ACTIVE` |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2.objectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2.cardinality` | enum | 是 | — |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2.foreignKeyPropertyApiName` | string | 否 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `ListOutgoingLinkTypesResponseV2.data.LinkTypeSideV2.linkTypeRid` | string | 是 | — |

```json
{
  "nextPageToken": "v1.QnVpbGQgdGhlIEZ1dHVyZTogaHR0cHM6Ly93d3cucGFsYW50aXIuY29tL2NhcmVlcnMvP2xldmVyLXNvdXJjZSU1YiU1ZD1BUElEb2NzI29wZW4tcG9zaXRpb25z",
  "data": [
    {
      "apiName": "originAirport",
      "objectTypeApiName": "Airport",
      "cardinality": "ONE",
      "foreignKeyPropertyApiName": "originAirportId"
    }
  ]
}
```
