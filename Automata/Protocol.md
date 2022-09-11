# Automata Protocol
Automata servers conform to the [JSONRPC 2.0 spec](https://www.jsonrpc.org/specification).
## Subscription Topics
### Sight
Returns a stream of all visible blocks within 30 blocks of the automaton with the supplied id.
#### Request
```json
{
  "jsonrpc": "2.0",
  "method": "subscribe",
  "params": {
    "topic": "sight",
    "params": {
      "automaton_id": "<UUID>"
    }
  },
  "id": 10
}
```
#### Success Response
```json
{"jsonrpc": "2.0", "result": "OK", "id": 10}
```
#### Stream
```json
{
  "jsonrpc": "2.0",
  "method": "subscribe",
  "params": {
    "topic": "sight",
    "content": [
      {
        "type": "dirt",
        "coordinate": {
          "x": 10,
          "y": 5,
          "z": 20
        }
      }
    ]
  }
}
```

## Commands
### Move
#### Request
Options for `direction` are `forward`, `back`, `left`, and `right`. 
Moves the automaton one block in the specified direction.
```json
{
  "jsonrpc": "2.0",
  "method": "move",
  "params": {
    "automaton_id": "<UUID>",
    "direction": "forward"
  },
  "id": 10
}
```
#### Success Response
```json
{"jsonrpc": "2.0", "result": "OK", "id": 10}
```

### Jump
#### Request
```json
{
  "jsonrpc": "2.0",
  "method": "jump",
  "params": {
    "automaton_id": "<UUID>"
  },
  "id": 10
}
```
#### Success Response
```json
{"jsonrpc": "2.0", "result": "OK", "id": 10}
```
### Turn
### Hit