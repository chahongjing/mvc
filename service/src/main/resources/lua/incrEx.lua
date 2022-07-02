local current = redis.call('incr', KEYS[1]);
redis.call('expire', KEYS[1], ARGV[1]);
return current;

-- print("单行注释")
--[[
print("整段注释")
print("整段注释")
]]