# Staff

An online staff directory for Box UK employees inspired by this blog post from Etsy.

http://codeascraft.com/2013/05/31/culture-hacking-with-a-staff-database/

This is part of a mobile app built by maxehmookau at

https://github.com/maxehmookau/staff-ios

There are so many new starters in Box UK it gets hard to keep track of new hires. It's also nice for new starters in a company to be able to get more information about who is who to prevent the awkward not knowing who to ask type situations.

![](https://raw.github.com/owainlewis/staff/master/public/images/preview.png)

Staff allows people to look up email addresses, find out information about what people are into, see who works where and quickly find the right person to answer their questions.

## Running the app

Staff is a Play2 application. You will need Scala, Postgres and SBT installed.

To get started 

+ download the most recent version of Play2
+ git clone git@github.com:boxuk/staff.git
+ cd staff

Create the postgres db

+ createdb staff

Run the application

+ play run

## Deploying the app

This application is built to run on Heroku easily. To deploy an instance to heroku run


```
heroku create --remote production
git push heroku production

# Check the logs if you have any issues
heroku logs

# Connect to the console
heroku run sbt play
```

## API

### Employees

Get all employees

```
GET /api/employees
```

Get a single employee by ID

```
GET /api/employees/:id
```

Get all employees with a given role (i.e all designers, developers etc)

```
curl -X GET -i http://localhost:9000/api/employees/roles/1
```

### Search

You can search for employees by first and last name

```
curl -X GET -i http://localhost:9000/api/search?q=jack
```

## Deployment

Staff is deployed and running on Heroku using Postgres

```
git push heroku master
```



