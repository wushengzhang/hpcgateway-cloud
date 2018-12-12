
def r = [  "result":false ];

if( request.method == "GET" )
{
    r.result = true;
}
else
{
    r.result = authentication.user.name == 'root';
}

return r;

