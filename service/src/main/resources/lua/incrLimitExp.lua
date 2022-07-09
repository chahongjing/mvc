local i = redis.call('get', KEYS[1]);
if (not i or tonumber(i) < tonumber(ARGV[1])) then
  local r = redis.call('incr', KEYS[1]);
  redis.call('expire', KEYS[1], tonumber(ARGV[2]));
  return r;
else
  return -1;
end;