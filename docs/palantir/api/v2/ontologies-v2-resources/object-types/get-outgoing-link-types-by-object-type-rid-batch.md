`POST /api/v2/ontologies/{ontology}/outgoingLinkTypes/getByRidBatch`

:::callout{theme=warning title=Warning}
  This endpoint is in preview and may be modified or removed at any time.
  To use this endpoint, add `preview=true` to the request query parameters.
:::

Gets outgoing link types for a batch of object types, identified by their RIDs.

For each requested object type, returns the list of outgoing link types visible to the
requesting token. Optionally, results can be filtered to only include specific link type RIDs.

Object types that don't exist or that the requesting token lacks permissions for are
silently omitted from the response.

The maximum batch size for this endpoint is 100.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `branch` | string | 否 | The Foundry branch to load the outgoing link type definitions from. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |
| `preview` | boolean | 否 | A boolean flag that, when set to true, enables the use of beta features in preview mode.<br>示例: `true` |

## Request body

## Response

**GetOutgoingLinkTypesByObjectTypeRidBatchResponse**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse` | object | 是 | Success response. |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data` | map | 否 | — |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.ObjectTypeRid` | string | 是 | The unique resource identifier of an object type, useful for interacting with other Foundry APIs. |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array` | list<LinkTypeSideV2> | 是 | — |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2` | object | 是 | `foreignKeyPropertyApiName` is the API name of the foreign key on this object type. If absent, the link is<br>either a m2m link or the linked object has the foreign key and this object type has the primary key. |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2.apiName` | string | 是 | The name of the link type in the API. To find the API name for your Link Type, check the **Ontology Manager**<br>application. |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2.displayName` | string | 是 | The display name of the entity. |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2.status` | enum | 是 | The release status of the entity.<br>示例: `ACTIVE` |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2.objectTypeApiName` | string | 是 | The name of the object type in the API in camelCase format. To find the API name for your Object Type, use the<br>`List object types` endpoint or check the **Ontology Manager**. |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2.cardinality` | enum | 是 | — |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2.foreignKeyPropertyApiName` | string | 否 | The name of the property in the API. To find the API name for your property, use the `Get object type`<br>endpoint or check the **Ontology Manager**. |
| `GetOutgoingLinkTypesByObjectTypeRidBatchResponse.data.array.LinkTypeSideV2.linkTypeRid` | string | 是 | — |
