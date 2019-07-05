module.exports = function(app, Contact)
{

    // GET ALL contacts
    app.get('/api/contact/all', function(req,res){
      Contact.find(function(err, contacts){
              if(err) return res.status(500).send({error: 'database failure'});
              res.json(contacts);
          })
    });

    // CREATE contact
    app.post('/api/contact/add', function(req, res){
      var contact = new Contact();
      contact.name = req.body.name;
      contact.phonenum = req.body.phonenum;
      contact.photo = req.body.photo;

      //저장시키기
      contact.save(function(err){
          if(err){
              console.error(err);
              res.json({result: 0});
              return;
          }
          res.json({result: 1});
      });
    });

    // DELETE contact
    app.delete('/api/contact/:contact_id', function(req, res){
      Contact.remove({ _id: req.params.contact_id }, function(err, output){
      if(err) return res.status(500).json({ error: "database failure" });

      /* ( SINCE DELETE OPERATION IS IDEMPOTENT, NO NEED TO SPECIFY )
      if(!output.result.n) return res.status(404).json({ error: "contact not found" });
      res.json({ message: "contact deleted" });
      */
      res.json({result: 1});
      res.status(204).end();
      })
    });

}
