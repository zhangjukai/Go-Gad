---
--- Generated by EmmyLua(https://github.com/EmmyLua)
--- Created by Administrator.
--- DateTime: 2021/2/4 10:32
---

-- redis.call("set",KEYS[1],redis.call("get", KEYS[1]) + 100)
return tostring((redis.call("get", KEYS[1]) + 100))