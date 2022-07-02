-- 未梳理
local notexists = redis.call('set', KEYS[1], 1, 'NX', 'EX', tonumber(ARGV[2]))
if (notexists) then
  return 1
end
local current = tonumber(redis.call('get', KEYS[1]))
if (not current) then
  local result = redis.call('incr', KEYS[1])
  redis.call('expire', KEYS[1], tonumber(ARGV[2]))
  return result
end
if (current >= tonumber(ARGV[1])) then
  error('too many requests')
end
return redis.call('incr', KEYS[1])