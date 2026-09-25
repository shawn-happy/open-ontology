`GET /api/v2/ontologies`

Lists the Ontologies visible to the current user.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Response

**ListOntologiesV2Response**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ListOntologiesV2Response` | object | 是 | Success response.<br>示例: `{"data":[{"apiName":"default-ontology","displayName":"Ontology","description":"The default ontology","rid":"ri.ontology.main.ontology.c61d9ab5-2919-4127-a0a1-ac64c0ce6367"},{"apiName":"shared-ontology","displayName":"Shared ontology","description":"The ontology shared with our suppliers","rid":"ri.ontology.main.ontology.c61d9ab5-2919-4127-a0a1-ac64c0ce6367"}]}` |
| `ListOntologiesV2Response.data` | list<OntologyV2> | 否 | The list of Ontologies the user has access to. |
| `ListOntologiesV2Response.data.OntologyV2` | object | 是 | Metadata about an Ontology. |
| `ListOntologiesV2Response.data.OntologyV2.apiName` | string | 是 | — |
| `ListOntologiesV2Response.data.OntologyV2.displayName` | string | 是 | The display name of the entity. |
| `ListOntologiesV2Response.data.OntologyV2.description` | string | 是 | — |
| `ListOntologiesV2Response.data.OntologyV2.rid` | string | 是 | The unique Resource Identifier (RID) of the Ontology. To look up your Ontology RID, please use the<br>`List ontologies` endpoint or check the **Ontology Manager**. |

```json
{
  "data": [
    {
      "apiName": "default-ontology",
      "displayName": "Ontology",
      "description": "The default ontology",
      "rid": "ri.ontology.main.ontology.c61d9ab5-2919-4127-a0a1-ac64c0ce6367"
    },
    {
      "apiName": "shared-ontology",
      "displayName": "Shared ontology",
      "description": "The ontology shared with our suppliers",
      "rid": "ri.ontology.main.ontology.c61d9ab5-2919-4127-a0a1-ac64c0ce6367"
    }
  ]
}
```
