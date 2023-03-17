-- Get all comments ordered by comment id
select *
from info
order by id;

-- Get whole comment tree
select *
from info
order by path;

-- Get first level comments ordered by id
select *
from info
where path ~ '*{1}'
order by id;

-- Get all children for comment with id=1
select *
from info
where path ~ '1.*{1}'
order by path;

-- Get all children for comment with id=3
select *
from info
where path ~ '3.*'
order by path;

-- Get second level comments / children for comment with id=1
select *
from info
where path ~ '1.*{1}'
order by path;

-- Get third level comments / children for comment with id=1
select *
from info
where path ~ '1.*{1}.*{1}'
order by path;