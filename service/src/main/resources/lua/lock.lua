local v = redis.call('set', KEYS[1], ARGV[1], 'NX', 'EX', ARGV[2]);
if v then
  return 1;
else
  if redis.call('get', KEYS[1]) == ARGV[1] then
    return 1;
  end
  return 0;
end