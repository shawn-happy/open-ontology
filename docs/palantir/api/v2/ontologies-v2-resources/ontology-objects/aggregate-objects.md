`POST /api/v2/ontologies/{ontology}/objects/{objectType}/aggregate`

Perform functions on object fields in the specified ontology and object type.


Third-party applications using this endpoint via OAuth2 must request the following operation scope: `api:ontologies-read`.

**OAuth2 scopes**: `api:ontologies-read`

## Path parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `ontology` | string | 是 | The API name or RID of the Ontology. To find the API name or RID, use the **List Ontologies** endpoint or<br>check the **Ontology Manager**.<br>示例: `palantir` |
| `objectType` | string | 是 | The type of the object to aggregate on.<br>示例: `employee` |

## Query parameters

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `sdkPackageRid` | string | 否 | The package rid of the generated SDK. |
| `sdkVersion` | string | 否 | The version of the generated SDK. |
| `branch` | string | 否 | The Foundry branch to aggregate objects from. If not specified, the default branch will be used.<br>Branches are an experimental feature and not all workflows are supported.<br>示例: `ri.branch..branch.d827184f-ee0e-4351-8b70-efbe51e07252` |

## Request body

```json
{
  "aggregation": [
    {
      "type": "min",
      "field": "tenure",
      "name": "min_tenure"
    },
    {
      "type": "avg",
      "field": "tenure",
      "name": "avg_tenure"
    }
  ],
  "where": {
    "type": "eq",
    "field": "name",
    "value": "john"
  },
  "groupBy": [
    {
      "field": "startDate",
      "type": "range",
      "ranges": [
        {
          "startValue": "2020-01-01",
          "endValue": "2020-06-01"
        }
      ]
    },
    {
      "field": "city",
      "type": "exact"
    }
  ]
}
```

## Response

**AggregateObjectsResponseV2**

Success response.

| 参数 | 类型 | 必填 | 说明 |
| --- | --- | --- | --- |
| `AggregateObjectsResponseV2` | object | 是 | Success response.<br>示例: `{"data":[{"metrics":[{"name":"min_tenure","value":1},{"name":"avg_tenure","value":3}],"group":{"startDate":{"startValue":"2020-01-01","endValue":"2020-06-01"},"city":"New York City"}},{"metrics":[{"name":"min_tenure","value":2},{"name":"avg_tenure","value":3}],"group":{"startDate":{"startValue":"2020-01-01","endValue":"2020-06-01"},"city":"San Francisco"}}]}` |
| `AggregateObjectsResponseV2.excludedItems` | integer | 否 | — |
| `AggregateObjectsResponseV2.accuracy` | enum | 是 | — |
| `AggregateObjectsResponseV2.data` | list<AggregateObjectsResponseItemV2> | 否 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2` | object | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.group` | map | 否 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.group.AggregationGroupKeyV2` | string | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.group.AggregationGroupValueV2` | any | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics` | list<AggregationMetricResultV2> | 否 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics.AggregationMetricResultV2` | object | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics.AggregationMetricResultV2.name` | string | 是 | — |
| `AggregateObjectsResponseV2.data.AggregateObjectsResponseItemV2.metrics.AggregationMetricResultV2.value` | any | 否 | The value of the metric. This will be a double in the case of<br>a numeric metric, or a date string in the case of a date metric. |
| `AggregateObjectsResponseV2.computeUsage` | number | 否 | A measurement of compute usage expressed in [compute-seconds](/docs/foundry/resource-management/usage-types#compute-second). For more information, please refer to the [Usage types](/docs/foundry/resource-management/usage-types) documentation.<br>示例: `3.7` |

```json
{
  "data": [
    {
      "metrics": [
        {
          "name": "min_tenure",
          "value": 1
        },
        {
          "name": "avg_tenure",
          "value": 3
        }
      ],
      "group": {
        "startDate": {
          "startValue": "2020-01-01",
          "endValue": "2020-06-01"
        },
        "city": "New York City"
      }
    },
    {
      "metrics": [
        {
          "name": "min_tenure",
          "value": 2
        },
        {
          "name": "avg_tenure",
          "value": 3
        }
      ],
      "group": {
        "startDate": {
          "startValue": "2020-01-01",
          "endValue": "2020-06-01"
        },
        "city": "San Francisco"
      }
    }
  ]
}
```
