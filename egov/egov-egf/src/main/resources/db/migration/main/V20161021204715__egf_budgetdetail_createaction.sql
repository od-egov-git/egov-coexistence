INSERT into EG_ACTION values (nextval('SEQ_EG_ACTION'),'BudgetCreate','/budget/budgetProposalDetail-createRE.action',null,(select id from eg_module where name='EGF-COMMON'),null,null,false,'EGF',0,1,now(),1,now(),(select id from eg_module where name = 'EGF' and parentmodule is null));

Insert into eg_roleaction   values((select id from eg_role where name='Super User'),(select id from eg_action where name='BudgetCreate'));
